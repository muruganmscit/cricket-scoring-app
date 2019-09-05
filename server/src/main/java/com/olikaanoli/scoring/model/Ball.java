package com.olikaanoli.scoring.model;

import com.olikaanoli.scoring.config.Extras;
import com.olikaanoli.scoring.config.Wickets;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "innings")
public class Ball {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique id for ball")
    private Long id;

    // it can be either 1 / 2
    private int innings;

    // unique id for the match
    @Column(name = "match_id")
    private int matchId;

    // team that plays batting
    @Column(name = "team_id")
    private int teamId;

    // over that is currently running
    private int overs;

    // current ball
    private int ball;

    // player facing the ball
    private int batsman;

    // player who delivers the ball
    private int bowler;

    // player who is not facing the ball
    @Column(name = "non_striker")
    private int nonStriker;

    // type of extras if any. this should come from a enum
    @Column(name = "extra_type")
    private Extras extraType = null;

    // any runs coming from extras
    @Column(name = "extra_run")
    private Integer extraRun = null;

    // Total runs scored in the ball
    @Column(name = "runs_total")
    private int runsTotal = 0;

    // runs scored nby batsman
    @Column(name = "batsman_runs")
    private int batsmanRuns = 0;

    // ball counter for batsman
    @Column(name = "batsman_ball")
    private int batsmanBall = 0;

    // runs allocated for bowler
    @Column(name = "bowler_runs")
    private int bowlerRuns = 0;

    // ball counter for bowler
    @Column(name = "bowler_ball")
    private int bowlerBall = 0;

    // if wicket, what type it is
    @Column(name = "wicket_type")
    private Wickets wicketType;

    // player who got out
    @Column(name = "wicket_player")
    private Integer wicketPlayer = null;

    // player who is involved in the wicket
    private Integer fielder1 = null;

    // player who is involved in the wicket
    private Integer fielder2 = null;

    // if the runs scored came by boundary
    @Column(name = "non_boundary")
    private Boolean nonBoundary = false;


}
