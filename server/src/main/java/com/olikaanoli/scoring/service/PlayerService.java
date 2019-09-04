package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.input.player.PlayerInput;
import com.olikaanoli.scoring.model.Player;
import com.olikaanoli.scoring.model.Team;
import com.olikaanoli.scoring.repository.PlayerRepository;
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
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final MapperFacade mapperFacade;
    private final TeamService teamService;

    public PlayerService(PlayerRepository playerRepository, TeamService teamService, MapperFacade mapperFacade) {
        this.playerRepository = playerRepository;
        this.mapperFacade = mapperFacade;
        this.teamService = teamService;
    }

    /**
     * Getting all teams
     */
    @GraphQLQuery(name = "GetAllPlayers")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Getting a team by Id
     */
    @GraphQLQuery(name = "GetPlayerById")
    public Optional<Player> getPlayerById(@GraphQLArgument(name = "playerId") Long playerId) {
        return playerRepository.findById(playerId);
    }

    @GraphQLMutation(name = "CreatePlayer")
    public Player createPlayer(@GraphQLArgument(name = "player") PlayerInput player) {
        Player locPlayer = new Player();
        mapperFacade.map(player, locPlayer);

        // setting the team details
        locPlayer.setPlayerTeam(teamService.getTeamById(player.getTeamId()).get());

        return playerRepository.save(locPlayer);
    }

    @GraphQLMutation(name = "UpdatePlayer")
    public Player updatePlayer(
            @GraphQLArgument(name = "playerId") Long playerId,
            @GraphQLArgument(name = "player") PlayerInput player
    ) {
        Player locPlayer = getPlayerById(playerId).get();
        mapperFacade.map(player, locPlayer);

        return playerRepository.save(locPlayer);
    }
}
