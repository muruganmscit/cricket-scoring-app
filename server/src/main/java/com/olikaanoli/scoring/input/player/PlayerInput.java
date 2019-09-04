package com.olikaanoli.scoring.input.player;

import com.olikaanoli.scoring.config.Gender;
import com.olikaanoli.scoring.config.Hands;
import com.olikaanoli.scoring.config.PlayerRoles;
import com.olikaanoli.scoring.model.Team;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
public class PlayerInput {

    // first name of the player
    private String firstName;

    // Player last name
    private String lastName;

    private Gender gender;

    private String nickName;

    private PlayerRoles playerRole;

    private Hands battingHand;

    private Hands bowlingHand;

    private Long teamId;
}
