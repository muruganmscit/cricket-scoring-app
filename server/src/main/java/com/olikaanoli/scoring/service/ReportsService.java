package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.model.response.BallByBallResponse;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@GraphQLApi
public class ReportsService {

    private final TotalScorecardService totalScorecardService;
    private final BowlerScorecardService bowlerScorecardService;
    private final BatsmanScorecardService batsmanScorecardService;
    private final BallsService ballsService;

    public ReportsService(
            @Lazy TotalScorecardService totalScorecardService,
            @Lazy BowlerScorecardService bowlerScorecardService,
            @Lazy BatsmanScorecardService batsmanScorecardService,
            @Lazy BallsService ballsService
    ) {
        this.totalScorecardService = totalScorecardService;
        this.bowlerScorecardService = bowlerScorecardService;
        this.batsmanScorecardService = batsmanScorecardService;
        this.ballsService = ballsService;
    }

    /**
     * Return the total and should be based on match id
     *
     * @return BallResponse
     */
    @GraphQLQuery(name = "GetMatchScorecard")
    public BallByBallResponse getMatchScorecard(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "teamId") Long teamId
    ) {
        // Ball Response
        // Getting the current over
        Integer overs = ballsService.getRunningOverForMatchIdAndTeam(matchId, teamId);
        if (null == overs) {
            overs = 0;
        }
        System.out.println("Overs: " + overs);
        return generateScoreCardReport(matchId, teamId, overs);
    }

    /**
     * Method: generateScoreCardReport
     *
     * @return
     */
    private BallByBallResponse generateScoreCardReport(Long matchId, Long teamId, int overs) {

        // init
        BallByBallResponse ballByBallResponse = new BallByBallResponse();

        // Getting the totals for the match
        ballByBallResponse.setTotalScorecards(totalScorecardService.getTotalCardByMatch(matchId));

        // Getting the Current bowler details
        ballByBallResponse.setBowlerScorecard(bowlerScorecardService.getCurrentBowlerForMatch(matchId));

        // Getting the Current Batsman details
        ballByBallResponse.setBatsmanScorecards(
                batsmanScorecardService.getAllActiveBatsmanByMatchAndTeam(matchId, teamId)
        );

        // getting the balls details
        ballByBallResponse.setBalls(ballsService.getAllBallsByMatchIdAndTeamIdAndOvers(
                matchId,
                teamId,
                overs,
                new Sort(Sort.Direction.ASC, "id")
        ));

        // return result
        return ballByBallResponse;
    }

}
