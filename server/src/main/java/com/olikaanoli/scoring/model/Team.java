package com.olikaanoli.scoring.model;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "Teams")
public class Team {

    @Id
    @GeneratedValue
    @GraphQLQuery(name = "id", description = "Unique id for a team")
    private Long id;

    @Column(nullable = false, name = "team_name")
    @GraphQLQuery(name = "teamName", description = "Name of the Team")
    private String teamName;

    @Column(nullable = false, name = "city")
    @GraphQLQuery(name = "city", description = "City of the Team")
    private String city;

    @OneToMany(mappedBy = "homeTeam")
    private Set<Match> homeMatches;

    @OneToMany(mappedBy = "awayTeam")
    private Set<Match> awayMatches;

    @OneToMany(mappedBy = "winningTeam")
    private Set<Match> winningMatches;

    @OneToMany(mappedBy = "tossWinner")
    private Set<Match> tossMatches;
}
