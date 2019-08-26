package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.model.Team;
import com.olikaanoli.scoring.repository.TeamRepository;
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
import java.util.Optional;

@Service
@GraphQLApi
public class TeamService {

    private final TeamRepository teamRepository;
    private final MapperFacade mapperFacade;
    private final ConcurrentMultiMap<String, FluxSink<Team>> subscribers = new ConcurrentMultiMap<>();

    public TeamService(TeamRepository teamRepository, MapperFacade mapperFacade) {
        this.teamRepository = teamRepository;
        this.mapperFacade = mapperFacade;
    }

    /**
     * adding a new team
     */
    @GraphQLMutation(name = "CreateTeam")
    public Team createTeam(@GraphQLArgument(name = "team") Team team) {

        Team locTeam = teamRepository.save(team);

        //Notify all the subscribers following this task
        subscribers.get("addTeam").forEach(subscriber -> subscriber.next(locTeam));
        return locTeam;
    }

    /**
     * Getting all teams
     */
    @GraphQLQuery(name = "GetAllTeams")
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Getting a team by Id
     */
    @GraphQLQuery(name = "GetTeamById")
    public Optional<Team> getTeamById(@GraphQLArgument(name = "teamId") Long teamId) {
        return teamRepository.findById(teamId);
    }

    /**
     * Updating the team Details
     */
    @GraphQLMutation(name = "UpdateTeam")
    public Team updateTeam(@GraphQLArgument(name = "team") Team team, @GraphQLArgument(name = "teamId") Long teamId) {
        Team locTeam = getTeamById(teamId).get();
        mapperFacade.map(team, locTeam);

        return teamRepository.save(locTeam);
    }

    @GraphQLMutation(name = "DeleteTeam")
    public Team deleteTeam(@GraphQLArgument(name = "teamId") Long teamId) {
        Team team = getTeamById(teamId).get();
        teamRepository.delete(team);

        //Notify all the subscribers following this task
        subscribers.get("deleteTeam").forEach(subscriber -> subscriber.next(team));

        return team;
    }

    @GraphQLSubscription
    public Publisher<Team> teamAdded() {
        return Flux.create(
                subscriber ->
                        subscribers.add("addTeam", subscriber.onDispose(
                                () -> subscribers.remove("addTeam", subscriber))),
                FluxSink.OverflowStrategy.LATEST);
    }

    @GraphQLSubscription
    public Publisher<Team> teamDeleted() {
        return Flux.create(
                subscriber ->
                        subscribers.add("deleteTeam", subscriber.onDispose(
                                () -> subscribers.remove("deleteTeam", subscriber))),
                FluxSink.OverflowStrategy.LATEST);
    }
}
