package com.olikaanoli.scoring.model;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "total_scorecard")
public class TotalScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique id")
    private Long id;

    // it can be either 1 / 2
    private int innings;

    // unique id for the match
    @Column(name = "match_id")
    private Long matchId;

    // team id where the batsman belongs to
    @GraphQLQuery(name = "Team", description = "Player Team")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "FK_TOTAL_SC_TEAM"))
    private Team team;

    // runs received via byes extra
    private Integer byes = 0;

    // runs received via legbyes extra
    @Column(name = "leg_byes")
    private Integer legByes = 0;

    // runs scored in NoBall
    @Column(name = "no_balls")
    private Integer noBalls = 0;

    // runs received by penalty
    private Integer penalty = 0;

    // runs received via wide extra
    private Integer wides = 0;

    // total runs received by extras ( wide / legbyes / byes / Noball)
    @Column(name = "extra_runs")
    private Integer extraRuns = 0;

    // Total Team runs
    @Column(name = "total_runs")
    private Integer totalRuns = 0;

    // Wickets count for the innings
    private Integer wickets = 0;

    // overs completed
    private int overs = 0;

    // current running balls
    private int balls = 0;

}
