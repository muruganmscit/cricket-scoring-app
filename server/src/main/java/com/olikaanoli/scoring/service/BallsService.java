package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.input.balls.BallInput;
import com.olikaanoli.scoring.model.Ball;
import com.olikaanoli.scoring.model.response.BallResponse;
import com.olikaanoli.scoring.repository.BallsRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import io.leangen.graphql.spqr.spring.util.ConcurrentMultiMap;
import ma.glasnost.orika.MapperFacade;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;

@Service
@GraphQLApi
public class BallsService {

    private final String BALL_ADDED = "_BallAdded";
    // private int total;
    // private int wicket;
    // private int extras;
    private final BallsRepository ballsRepository;
    private final MapperFacade mapperFacade;
    private final ConcurrentMultiMap<String, FluxSink<BallResponse>> subscribers = new ConcurrentMultiMap<>();

    public BallsService(BallsRepository ballsRepository, MapperFacade mapperFacade) {
        this.ballsRepository = ballsRepository;
        this.mapperFacade = mapperFacade;
        //this.total = 0;
        //this.wicket = 0;
        //this.extras = 0;
    }

    /**
     * Saving the given ball
     */
    @GraphQLMutation(name = "AddBall")
    public Ball saveBall(@GraphQLArgument(name = "ballInput") BallInput ballInput) {

        // creating the ball Object from Input
        Ball localBall = mapperFacade.map(ballInput, Ball.class);

        // checking whether the extra type is available
        // checking for legal ball
        if(null == localBall.getExtraType()) {
            // setting the batsman and bowler runs & Balls
            localBall.setBatsmanRuns(ballInput.getRunsTotal());
            localBall.setBatsmanBall(1);

            localBall.setBowlerRuns(ballInput.getRunsTotal());
            localBall.setBatsmanBall(1);
        }

        // saving the object to database
        ballsRepository.save(localBall);

        // calculate ball
        //calculateBall(locBall);

        // Notify all the subscribers following this task
        //String index = ball.getMatchId() + BALL_ADDED;

        //subscribers.get(index).forEach(subscriber -> subscriber.next(getTotal()));

        return localBall;
    }

    @GraphQLQuery(name = "GetAllBalls")
    public List<Ball> getAllBalls() {
        return ballsRepository.findAll();
    }

    /**
     * Return the total and should be based on match id
     *
     * @return
     */
    @GraphQLQuery(name = "GetTotal")
    public BallResponse getTotal() {
        // Ball Response
        BallResponse ballResponse = new BallResponse();
        return ballResponse;
    }

    @GraphQLSubscription(name = "BallAdded")
    public Publisher<BallResponse> ballAdded(int matchId) {
        String index = matchId + BALL_ADDED;
        return Flux.create(
                subscriber ->
                        subscribers.add(index, subscriber.onDispose(() ->
                                subscribers.remove(index, subscriber))),
                FluxSink.OverflowStrategy.LATEST);
    }
}
