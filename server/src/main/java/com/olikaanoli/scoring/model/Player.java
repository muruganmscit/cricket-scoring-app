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
    @Column(name = "first_name")
    private String firstName;

    // Player last name
    @Column(name = "last_name")
    private String lastName;

    private Gender gender;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "player_role")
    private PlayerRoles playerRole;

    @Column(name = "batting_hand")
    private Hands battingHand;

    @Column(name = "bowling_hand")
    private Hands bowlingHand;

    @GraphQLQuery(name = "Team", description = "Player Team")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "FK_TEAM"))
    private Team playerTeam;
}
