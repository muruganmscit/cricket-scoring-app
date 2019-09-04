package com.olikaanoli.scoring.model;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Teams")
@ToString(exclude = {"players", "homeTeam", "awayTeam", "tossWinner"})
@EqualsAndHashCode(exclude = {"players", "homeTeam", "awayTeam", "tossWinner"})
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

    @OneToMany(mappedBy = "playerTeam")
    private Set<Player> players = new LinkedHashSet<>();

    /*@OneToMany(mappedBy = "homeTeam")
    private Set<Match> homeMatches = new LinkedHashSet<>();

    @OneToMany(mappedBy = "awayTeam")
    private Set<Match> awayMatches = new LinkedHashSet<>();

    @OneToMany(mappedBy = "winningTeam")
    private Set<Match> winningMatches = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tossWinner")
    private Set<Match> tossMatches = new LinkedHashSet<>();*/
}
