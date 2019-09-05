package com.olikaanoli.scoring.model;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wicket_scorecard")
public class WicketScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique id")
    private Long id;

    // unique id for the match
    @Column(name = "match_id")
    private int matchId;

    // team id where the batsman belongs to
    @Column(name = "team_id")
    private int teamId;

    // wicket number
    @Column(name = "wicket_no")
    private int wicketNo;

    // Total Runs when the wicket fell
    private int runs;
}
