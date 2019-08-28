package com.aexp.samplespqr.model;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(exclude = "books")
@EqualsAndHashCode(exclude = "books")
@Table(name = "balls")
public class Ball {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLQuery(name = "id", description = "Unique id for ball")
    private Long id;

    @Column(nullable = false, name = "match_id")
    private int matchId;

    @Column(nullable = false, name = "ball_details")
    private String ballDetails;
}
