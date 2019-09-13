package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.model.BatsmanScorecard;
import com.olikaanoli.scoring.repository.BatsmanScorecardRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@GraphQLApi
public class BatsmanScorecardService {

    private final BatsmanScorecardRepository batsmanScorecardRepository;
    private final SubscriptionService subscriptionService;

    public BatsmanScorecardService(
            BatsmanScorecardRepository batsmanScorecardRepository,
            SubscriptionService subscriptionService
    ) {
        this.batsmanScorecardRepository = batsmanScorecardRepository;
        this.subscriptionService = subscriptionService;
    }

    @GraphQLQuery(name = "GetAllBatsmanByMatchAndTeam")
    public List<BatsmanScorecard> getAllBatsmanByMatchAndTeam(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "teamId") Long teamId
    ) {
        // TODO: the ordering logic has to be modified
        return batsmanScorecardRepository.findAllByMatchIdAndTeamId(
                matchId,
                teamId,
                new Sort(Sort.Direction.ASC, "battingOrder", "batsmanId.firstName")
        );
    }

    @GraphQLQuery(name = "GetAllActiveBatsmanByMatchAndTeam")
    public List<BatsmanScorecard> getAllActiveBatsmanByMatchAndTeam(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "teamId") Long teamId
    ) {
        return batsmanScorecardRepository.findAllActiveBatsmanScorecardByMatchIdAndTeamId(matchId, teamId);
    }

    @GraphQLQuery(name = "GetBattingScorecardByBatsmanIdAndMatchId")
    public BatsmanScorecard getBattingScorecardByBatsmanIdAndMatchId(
            @GraphQLArgument(name = "batsmanId") Long batsmanId,
            @GraphQLArgument(name = "matchId") Long matchId
    ) {
        return batsmanScorecardRepository.findByBatsman_IdAndMatchId(batsmanId, matchId);
    }

    /**
     * Setting a batsman as striker / non striker
     */
    @GraphQLMutation(name = "SetNewBatsman")
    public Boolean setNewBatsman(
            @GraphQLArgument(name = "batsmanId") Long batsmanId,
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "isOnStrike") Boolean isOnStrike
    ) {
        int batting = 1;
        int nonStriker = 2;
        List<BatsmanScorecard> submitList = new ArrayList<>();

        // Getting the batsman current record for updates
        BatsmanScorecard batsmanToBeSet = getBattingScorecardByBatsmanIdAndMatchId(batsmanId, matchId);

        // Updating the batting flag and comment the records
        // this table will have a trigger which will reset other batsman flag
        // accordingly
        if(!isOnStrike) {
            batting = 2;
            nonStriker = 1;
        }
        batsmanToBeSet.setBatting(batting);
        submitList.add(batsmanToBeSet);

        // Getting the existing batsman with same status
        // TODO: Need to check on the logic. if by some mishap if we received
        // TODO: 2 records
        BatsmanScorecard existingBatsman = batsmanScorecardRepository.findByTeamIdAndMatchIdAndBatting(
                batsmanToBeSet.getTeam().getId(), matchId, batting
        );
        if(null != existingBatsman) {
            existingBatsman.setBatting(nonStriker);
            submitList.add(existingBatsman);
        }

        // calling the update method
        batsmanScorecardRepository.saveAll(submitList);

        // update the subscribers
        subscriptionService.postToSubscribers(matchId, batsmanToBeSet.getTeam().getId());

        return true;
    }

    /**
     * Getting all the not out batsman
     */
    @GraphQLQuery(name = "GetAllNotOutBatsman")
    public List<BatsmanScorecard> getAllNotOutBatsman(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "teamId") Long teamId
    ) {
        return batsmanScorecardRepository.findAllBatsmanScorecardByMatchIdAndTeamIdAndBatting(
                matchId, teamId
        );
    }

}
