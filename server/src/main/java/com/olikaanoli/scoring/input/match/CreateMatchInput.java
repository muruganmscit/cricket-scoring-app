package com.olikaanoli.scoring.input.match;

import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

@Data
public class CreateMatchInput {

    @GraphQLNonNull
    private Long homeTeamId;

    @GraphQLNonNull
    private Long awayTeamId;

    @GraphQLNonNull
    @GraphQLQuery(name = "matchDate")
    private String dates;

    @GraphQLNonNull
    private Integer overs;

    @GraphQLNonNull
    private String city;

    @GraphQLNonNull
    private String venue;

}
