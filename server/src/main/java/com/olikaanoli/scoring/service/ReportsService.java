package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.config.InningStatus;
import com.olikaanoli.scoring.model.response.BallByBallResponse;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import io.swagger.models.auth.In;
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
    private final MatchService matchService;

    public ReportsService(
            @Lazy TotalScorecardService totalScorecardService,
            @Lazy BowlerScorecardService bowlerScorecardService,
            @Lazy BatsmanScorecardService batsmanScorecardService,
            @Lazy BallsService ballsService,
            @Lazy MatchService matchService
    ) {
        this.totalScorecardService = totalScorecardService;
        this.bowlerScorecardService = bowlerScorecardService;
        this.batsmanScorecardService = batsmanScorecardService;
        this.ballsService = ballsService;
        this.matchService = matchService;
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
     * Return the total and should be based on match id
     *
     * @return BallResponse
     */
    @GraphQLQuery(name = "GetMatchScorecardByMatch")
    public BallByBallResponse getMatchScorecardByMatch(
            @GraphQLArgument(name = "matchId") Long matchId
    ) {

        BallByBallResponse ballByBallResponse = new BallByBallResponse();

        // Getting the current Innings details
        // Get the values only for valid innings
        Integer innings = matchService.getInningsForMatch(matchId).getInningsStatusCode();
        if(innings != 4) {

            // Ball Response
            // Getting the current over
            Integer overs = ballsService.getRunningOverForMatchIdAndInnings(matchId, innings);
            if (null == overs) {
                overs = 0;
            }

            ballByBallResponse = generateScoreCardReportByInnings(matchId, innings, overs);
            System.out.println("Overs: " + overs);
        }

        // populating the match details
        ballByBallResponse.setMatch(matchService.getMatchById(matchId).get());

        // populating the playing 11 1st innings
        ballByBallResponse.setPlaying11Innings1(
                batsmanScorecardService.getAllBatsmanByMatchAndInnings(matchId, 1)
        );

        ballByBallResponse.setPlaying11Innings2(
                batsmanScorecardService.getAllBatsmanByMatchAndInnings(matchId, 2)
        );

        return ballByBallResponse;
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

    /**
     * Method: generateScoreCardReportByInnings
     *
     * @return
     */
    private BallByBallResponse generateScoreCardReportByInnings(Long matchId, Integer innings, int overs) {

        // init
        BallByBallResponse ballByBallResponse = new BallByBallResponse();

        // Getting the totals for the match
        ballByBallResponse.setTotalScorecards(totalScorecardService.getTotalCardByMatch(matchId));

        // Getting the Current bowler details
        ballByBallResponse.setBowlerScorecard(bowlerScorecardService.getCurrentBowlerForMatch(matchId));

        // Getting the Current Batsman details
        ballByBallResponse.setBatsmanScorecards(
                batsmanScorecardService.getAllActiveBatsmanByMatchAndInnings(matchId, innings)
        );

        // getting the balls details
        ballByBallResponse.setBalls(ballsService.getAllBallsByMatchIdAndInningsAndOvers(
                matchId,
                innings,
                overs,
                new Sort(Sort.Direction.ASC, "id")
        ));

        // return result
        return ballByBallResponse;
    }

}
