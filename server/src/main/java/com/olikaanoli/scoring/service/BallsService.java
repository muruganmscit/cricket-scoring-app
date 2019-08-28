package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.model.Ball;
import com.olikaanoli.scoring.model.BallResponse;
import com.olikaanoli.scoring.model.Team;
importcom.olikaanoli.scoring.repository.BallsRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import io.leangen.graphql.spqr.spring.util.ConcurrentMultiMap;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;

@Service
@GraphQLApi
public class BallsService {

    private final String BALL_ADDED = "_BallAdded";
    private int total;
    private int wicket;
    private int extras;
    private final BallsRepository ballsRepository;
    private final ConcurrentMultiMap<String, FluxSink<BallResponse>> subscribers = new ConcurrentMultiMap<>();

    public BallsService(BallsRepository ballsRepository) {
        this.ballsRepository = ballsRepository;
        this.total = 0;
        this.wicket = 0;
        this.extras = 0;
    }

    /**
     * Saving the given ball
     */
    @GraphQLMutation(name = "AddBall")
    public Ball saveBall(@GraphQLArgument(name = "ball") Ball ball) {
        Ball locBall = ballsRepository.save(ball);

        // calculate ball
        calculateBall(locBall);

        // Notify all the subscribers following this task
        String index = ball.getMatchId() + BALL_ADDED;

        subscribers.get(index).forEach(subscriber -> subscriber.next(getTotal()));

        return locBall;
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
        //ballResponse.setMatchId(ball.getMatchId());
        ballResponse.setTotal(this.total);
        ballResponse.setWickets(this.wicket);
        ballResponse.setExtras(this.extras);

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

    private void calculateBall(Ball ball) {
        switch (ball.getBallDetails()) {
            case "W":
                this.wicket = this.wicket + 1;
                break;
            case "N":
                this.extras = this.extras + 1;
                break;
            case "O":
                this.total = this.total + 1;
                break;
            case "T":
                this.total = this.total + 2;
                break;
            case "F":
                this.total = this.total + 4;
                break;
        }
    }
}
