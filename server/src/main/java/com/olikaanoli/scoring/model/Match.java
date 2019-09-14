package com.olikaanoli.scoring.model;

import com.olikaanoli.scoring.config.InningStatus;
import com.olikaanoli.scoring.config.TossDecision;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique identifier for match")
    private Long id;

    @Column(nullable = false, name = "city")
    @GraphQLQuery(name = "city", description = "City where the match happens.")
    private String city;

    @Column(nullable = false, name = "dates")
    @GraphQLQuery(name = "dates", description = "Match Dates.")
    private String dates;

    @Column(name = "overs", nullable = false)
    @GraphQLQuery(name = "overs", description = "No of overs for Match")
    private Integer overs;

    @GraphQLQuery(name = "homeTeam", description = "Home Team")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", foreignKey = @ForeignKey(name = "FK_HOME_TEAM"))
    private Team homeTeam;

    @GraphQLQuery(name = "awayTeam", description = "Away Team")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", foreignKey = @ForeignKey(name = "FK_AWAY_TEAM"))
    private Team awayTeam;

    @Column(name = "venue", nullable = false)
    @GraphQLQuery(name = "venue", description = "Venue where match happens")
    private String venue;

    @Column(name = "outcome_by")
    @GraphQLQuery(name = "outcomeBy", description = "Match was won by runs / wickets")
    private String outcomeBy;

    @Column(name = "difference")
    @GraphQLQuery(name = "difference", description = "Match won by what difference.")
    private Integer difference;

    @GraphQLQuery(name = "winningTeam", description = "Team won the Match")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winning_team_id", foreignKey = @ForeignKey(name = "FK_WINNING_TEAM"))
    private Team winningTeam;

    @GraphQLQuery(name = "tossWinner", description = "Toss Winner")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toss_team_id", foreignKey = @ForeignKey(name = "FK_TOSS_TEAM"))
    private Team tossWinner;

    @Column(name = "toss_decision")
    @GraphQLQuery(name = "tossDecision", description = "Toss Decision")
    private TossDecision tossDecision;

    @Column(name = "umpire1")
    @GraphQLQuery(name = "umpire1", description = "Name of first umpire")
    private String umpire1;

    @Column(name = "umpire2")
    @GraphQLQuery(name = "umpire2", description = "Name of second umpire")
    private String umpire2;

    // setting the current innings
    @Column(name = "running_innings")
    private InningStatus currentInnings;

    public Integer getCurrentInnings() {
        return currentInnings.getInningsStatusCode();
    }
}
