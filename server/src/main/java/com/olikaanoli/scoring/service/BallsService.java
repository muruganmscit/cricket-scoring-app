package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.input.balls.BallInput;
import com.olikaanoli.scoring.model.Ball;
import com.olikaanoli.scoring.model.Player;
import com.olikaanoli.scoring.model.response.BallByBallResponse;
import com.olikaanoli.scoring.repository.BallsRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import io.leangen.graphql.spqr.spring.util.ConcurrentMultiMap;
import ma.glasnost.orika.MapperFacade;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@GraphQLApi
public class BallsService {

    private final String BALL_ADDED = "_BallAdded";

    private final BallsRepository ballsRepository;
    private final MapperFacade mapperFacade;
    private final ConcurrentMultiMap<String, FluxSink<BallByBallResponse>> subscribers = new ConcurrentMultiMap<>();
    private final BallCalculationRulesService ballCalculationRulesService;
    private final PlayerService playerService;
    private final TotalScorecardService totalScorecardService;
    private final BowlerScorecardService bowlerScorecardService;
    private final BatsmanScorecardService batsmanScorecardService;

    public BallsService(
            BallsRepository ballsRepository,
            MapperFacade mapperFacade,
            BallCalculationRulesService ballCalculationRulesService,
            PlayerService playerService,
            TotalScorecardService totalScorecardService,
            BowlerScorecardService bowlerScorecardService,
            BatsmanScorecardService batsmanScorecardService
    ) {
        this.ballsRepository = ballsRepository;
        this.mapperFacade = mapperFacade;
        this.ballCalculationRulesService = ballCalculationRulesService;
        this.playerService = playerService;
        this.totalScorecardService = totalScorecardService;
        this.bowlerScorecardService = bowlerScorecardService;
        this.batsmanScorecardService = batsmanScorecardService;
    }

    /**
     * Saving the given ball
     */
    @GraphQLMutation(name = "AddBall")
    public Ball saveBall(@GraphQLArgument(name = "ballInput") BallInput ballInput) {

        Map<Integer, Player> playerMap = new HashMap<>();

        // creating the ball Object from Input
        Ball localBall = mapperFacade.map(ballInput, Ball.class);

        // Call the Drool kie for updating the runs, balls and extras
        ballCalculationRulesService.calculateBall(localBall);

        // saving the object to database
        ballsRepository.save(localBall);

        // Notify all the subscribers following this task
        String index = localBall.getMatchId() + BALL_ADDED;

        // calling method to populate the BallByBallResponse Object
        // and adding to sub
        subscribers.get(index).forEach(subscriber -> subscriber.next(
                getMatchScorecard(localBall.getMatchId(), localBall.getTeamId(), localBall.getOvers())
        ));

        return localBall;
    }

    @GraphQLQuery(name = "GetAllBalls")
    public List<Ball> getAllBalls() {
        return ballsRepository.findAll();
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
        Integer overs = ballsRepository.findRunningOverForMatchIdAndTeam(matchId, teamId);
        if (null == overs) {
            overs = 0;
        }
        return getMatchScorecard(matchId, teamId, overs);
    }

    @GraphQLSubscription(name = "BallAdded")
    public Publisher<BallByBallResponse> ballAdded(int matchId) {
        String index = matchId + BALL_ADDED;
        return Flux.create(
                subscriber ->
                        subscribers.add(index, subscriber.onDispose(() ->
                                subscribers.remove(index, subscriber))),
                FluxSink.OverflowStrategy.LATEST);
    }

    /**
     * Method: populateResponse
     *
     * @return
     */
    private BallByBallResponse getMatchScorecard(Long matchId, Long teamId, int overs) {

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
        ballByBallResponse.setBalls(ballsRepository.findAllBallsByMatchIdAndTeamIdAndOvers(
                matchId,
                teamId,
                overs,
                new Sort(Sort.Direction.ASC, "id")
        ));

        // return result
        return ballByBallResponse;
    }
}
