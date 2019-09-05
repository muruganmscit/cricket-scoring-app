package com.olikaanoli.scoring.model;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bowler_scorecard")
public class BowlerScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique id")
    private Long id;

    // unique id for the match
    @Column(name = "match_id")
    private Long matchId;

    // innings number
    private int innings;

    // team id where the bowler belongs to
    @GraphQLQuery(name = "Team", description = "Player Team")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "FK_BOW_SC_TEAM"))
    private Team team;

    // Bowler ID
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bowler_id", foreignKey = @ForeignKey(name = "FK_BOWLER_ID"))
    private Player bowler;

    // Order in which he / she is bowling
    // setting the value default to 12
    @Column(name = "bowling_order")
    private Integer bowlingOrder = 12;

    // no of overs bowler has bowled
    private Float overs = null;

    // no of over where no runs were scored.
    private Integer maidens = 0;

    // runs given by the bowler
    private Integer runs = 0;

    // wickets taken by the bowler
    private Integer wickets = 0;

    // No of dot balls
    @Column(name = "dot_balls")
    private Integer dotBalls = 0;

    // No Ball extra type
    @Column(name = "no_balls")
    private Integer noBalls = 0;

    // Wide extra type
    private Integer wides = 0;

    // flag to indicate who is the current bowler
    private Boolean bowling = false;

}
