package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.model.response.BallByBallResponse;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import io.leangen.graphql.spqr.spring.util.ConcurrentMultiMap;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Service
@GraphQLApi
public class SubscriptionService {

    private final String BALL_ADDED = "_BallAdded";
    private final ReportsService reportsService;

    private final ConcurrentMultiMap<String, FluxSink<BallByBallResponse>> subscribers = new ConcurrentMultiMap<>();

    public SubscriptionService(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GraphQLSubscription(name = "BallAdded")
    public Publisher<BallByBallResponse> ballAdded(int matchId) {
        String index = matchId + BALL_ADDED;
        //System.out.println("BallAdded: " + index);
        return Flux.create(
                subscriber ->
                        subscribers.add(index, subscriber.onDispose(() ->
                                subscribers.remove(index, subscriber))),
                FluxSink.OverflowStrategy.LATEST);
    }

    /**
     * This method will publish the details to the Subscribers
     *
     * @param matchId match id
     */
    public void postToSubscribers(Long matchId, Long teamId) {

        // Notify all the subscribers following this task
        String index = matchId + BALL_ADDED;
        //System.out.println("postToSubscribers: " + index);
        //System.out.println("postToSubscribers: " + subscribers.get(index).size());

        // calling method to populate the BallByBallResponse Object
        // and adding to sub
        subscribers.get(index).forEach(subscriber -> subscriber.next(
                reportsService.getMatchScorecard(matchId, teamId)
        ));
    }
}
