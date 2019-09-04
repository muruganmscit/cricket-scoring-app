package com.olikaanoli.scoring.model;

import com.olikaanoli.scoring.config.Gender;
import com.olikaanoli.scoring.config.Hands;
import com.olikaanoli.scoring.config.PlayerRoles;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique identifier for Player")
    private Long id;

    // first name of the player
    private String firstName;

    // Player last name
    private String lastName;

    private Gender gender;

    private String nickName;

    private PlayerRoles playerRole;

    private Hands battingHand;

    private Hands bowlingHand;

    @GraphQLQuery(name = "Team", description = "Player Team")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "FK_TEAM"))
    private Team playerTeam;
}
