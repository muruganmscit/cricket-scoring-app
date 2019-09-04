package com.olikaanoli.scoring.model;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "batsman_scorecard")
public class BatsmanScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique id")
    private Long id;

    // unique id for the match
    private Long matchId;

    // innings number
    private int innings;

    // team id where the batsman belongs to
    @GraphQLQuery(name = "Team", description = "Player Team")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "FK_BAT_SC_TEAM"))
    private Team team;

    // Batsman ID
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batsman_id", foreignKey = @ForeignKey(name = "FK_BATSMAN_ID"))
    private Player batsman;

    // Order in which he / she is batting
    // setting the value default to 12
    private Integer battingOrder = 12;

    // runs scored by batsman
    private Integer runs = 0;

    // balls faced by batsman
    private Integer balls = 0;

    // ones scored
    private Integer ones = 0;

    // twos scored
    private Integer twos = 0;

    // threes scored
    private Integer threes = 0;

    // Fours scored
    private Integer fours = 0;

    // Sixes Scored
    private Integer sixes = 0;

    // No of dot balls
    private Integer dotBalls = 0;

    // if out. what type of wicket.
    private Integer wicketType;

    // Bowler id who took the wicket
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bowler_id", foreignKey = @ForeignKey(name = "FK_WK_BOWLER_ID"))
    private Player bowler;

    // player who is involved in the wicket
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fielder1_id", foreignKey = @ForeignKey(name = "FK_F1_ID"))
    private Player fielder1 = null;

    // player who is involved in the wicket
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fielder2_id", foreignKey = @ForeignKey(name = "FK_F2_ID"))
    private Player fielder2 = null;

    // three options
    // 0. Out
    // 1. is he facing the bowler
    // 2. if he not facing the bowler. non striker
    // 3. Yet to bat
    private Integer batting = 3;

}
