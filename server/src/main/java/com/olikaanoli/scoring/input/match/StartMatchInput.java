package com.olikaanoli.scoring.input.match;

import com.olikaanoli.scoring.config.TossDecision;
import com.olikaanoli.scoring.model.Team;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
public class StartMatchInput {

    private Long innings1Team;

    // Home Team Playing 11
    private Set<Long> innings1Playing11;

    private Long innings2Team;

    // Away Team Playing 11
    private Set<Long> innings2Playing11;

    // team that won the toss
    private Long tossWinnerId;

    // decision made by the winning team batting / fielding
    private TossDecision tossDecision;
}
