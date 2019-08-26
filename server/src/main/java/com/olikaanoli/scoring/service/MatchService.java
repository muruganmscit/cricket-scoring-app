package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.input.match.CreateMatchInput;
import com.olikaanoli.scoring.model.Match;
import com.olikaanoli.scoring.repository.MatchRepository;
import com.olikaanoli.scoring.repository.TeamRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@GraphQLApi
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MapperFacade mapperFacade;

    public MatchService(
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            MapperFacade mapperFacade
    ) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.mapperFacade = mapperFacade;
    }

    /**
     * Function to get all the matchs
     */
    @GraphQLQuery(name = "GetAllMatchs")
    public List<Match> getMatchs() {
        return matchRepository.findAll();
    }

    /**
     * Function to get a single match details
     */
    @GraphQLQuery(name = "GetMatchById")
    public Optional<Match> getMatchById(@GraphQLArgument(name = "matchId") Long matchId) {
        return matchRepository.findById(matchId);
    }

    @GraphQLMutation(name = "CreateMatch")
    public Match saveMatch(@GraphQLArgument(name = "createMatchInput") CreateMatchInput match) {

        Match lMatch = mapperFacade.map(match, Match.class);

        // updating the home and away team details
        lMatch.setHomeTeam(teamRepository.findById(match.getHomeTeamId()).get());
        lMatch.setAwayTeam(teamRepository.findById(match.getAwayTeamId()).get());

        return matchRepository.save(lMatch);
    }

    /**
     * Updating the match details
     * @param match Match Object
     * @param matchId Match ID
     * @return match
     */
    @GraphQLMutation(name = "UpdateMatchDetails")
    public Match updateMatch(
            @GraphQLArgument(name = "match") Match match,
            @GraphQLArgument(name = "matchId") Long matchId) {
        Match locMatch = getMatchById(matchId).get();
        mapperFacade.map(match, locMatch);
        return matchRepository.save(locMatch);

    }
}
