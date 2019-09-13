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
    private final SubscriptionService subscriptionService;

    public BallsService(
            BallsRepository ballsRepository,
            MapperFacade mapperFacade,
            BallCalculationRulesService ballCalculationRulesService,
            SubscriptionService subscriptionService
    ) {
        this.ballsRepository = ballsRepository;
        this.mapperFacade = mapperFacade;
        this.ballCalculationRulesService = ballCalculationRulesService;
        this.subscriptionService = subscriptionService;
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

        // calling method to populate the BallByBallResponse Object
        // and adding to sub
        /*subscribers.get(index).forEach(subscriber -> subscriber.next(
                getMatchScorecard(localBall.getMatchId(), localBall.getTeamId(), localBall.getOvers())
        ));*/
        subscriptionService.postToSubscribers(localBall.getMatchId(), localBall.getTeamId());

        return localBall;
    }

    @GraphQLQuery(name = "GetAllBalls")
    public List<Ball> getAllBalls() {
        return ballsRepository.findAll();
    }

    /**
     * getRunningOverForMatchIdAndTeam
     *
     * Returns the current running over for the match and team
     *
     * @param matchId
     * @param teamId
     * @return
     */
    public Integer getRunningOverForMatchIdAndTeam(Long matchId, Long teamId) {
        return ballsRepository.findRunningOverForMatchIdAndTeam(matchId, teamId);
    }

    public List<Ball> getAllBallsByMatchIdAndTeamIdAndOvers(
            Long matchId, Long teamId, int overs, Sort sort
    ) {
        return ballsRepository.findAllBallsByMatchIdAndTeamIdAndOvers(matchId, teamId, overs, sort);
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
}
