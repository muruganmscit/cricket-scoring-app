package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.model.BowlerScorecard;
import com.olikaanoli.scoring.repository.BowlerScorecardRepository;
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
public class BowlerScorecardService {

    private final BowlerScorecardRepository bowlerScorecardRepository;

    public BowlerScorecardService(BowlerScorecardRepository bowlerScorecardRepository) {
        this.bowlerScorecardRepository = bowlerScorecardRepository;
    }

    @GraphQLQuery(name = "GetAllBowlersByMatchAndTeam")
    public List<BowlerScorecard> getAllBowlersByMatchAndTeam(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "teamId") Long teamId
    ) {
        // TODO: the ordering logic has to be modified
        return bowlerScorecardRepository.findAllBowlersByMatchIdAndTeamId(
                matchId,
                teamId,
                new Sort(Sort.Direction.ASC, "bowlingOrder", "bowler.firstName")
        );
    }

    @GraphQLQuery(name = "GetBowlerScorecardById")
    public BowlerScorecard getBowlerScorecardById(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "bowlerId") Long bowlerId
    ) {
        return bowlerScorecardRepository.findByBowler_IdAndMatchId(bowlerId, matchId);
    }

    @GraphQLQuery(name = "GetCurrentBowlerForMatch")
    public BowlerScorecard getCurrentBowlerForMatch(
            @GraphQLArgument(name = "matchId") Long matchId
    ) {
        return bowlerScorecardRepository
                .findCurrentBowlerByMatchId(matchId);
    }

    @GraphQLMutation(name = "SetNewBowler")
    public Boolean setNewBowler(
            @GraphQLArgument(name = "bowlerId") Long bowlerId,
            @GraphQLArgument(name = "matchId") Long matchId
    ) {
        List<BowlerScorecard> updateList = new ArrayList<>();

        // Get the bowler details
        BowlerScorecard updateBowler = getBowlerScorecardById(matchId, bowlerId);
        updateBowler.setBowling(true);
        updateList.add(updateBowler);
        System.out.println(updateBowler.getBowler().getId() + " - " + updateBowler.getTeam().getTeamName());

        // Get the current bowler and reset the bowling flag if any
        BowlerScorecard currentBowler =
                bowlerScorecardRepository
                        .findCurrentBowlerByMatchId(matchId);
        if(null != currentBowler) {
            currentBowler.setBowling(false);
            System.out.println(currentBowler.getId() + " - "
                    + currentBowler.getTeam().getTeamName() + " - " + currentBowler.getBowling()) ;
            updateList.add(currentBowler);
        }

        // submit the updates
        bowlerScorecardRepository.saveAll(updateList);

        return true;
    }
}
