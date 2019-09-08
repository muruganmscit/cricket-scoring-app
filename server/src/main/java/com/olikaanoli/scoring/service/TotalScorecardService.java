package com.olikaanoli.scoring.service;

import com.olikaanoli.scoring.model.TotalScorecard;
import com.olikaanoli.scoring.repository.TotalScorecardRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@GraphQLApi
public class TotalScorecardService {

    @Autowired
    TotalScorecardRepository totalScorecardRepository;

    @GraphQLQuery(name = "GetTotalCardByTeamAndMatch")
    public TotalScorecard getTotalCardByTeamAndMatch(
            @GraphQLArgument(name = "matchId") Long matchId,
            @GraphQLArgument(name = "teamId") Long teamId
    ) {
        return totalScorecardRepository.findByMatchIdAndTeamId(matchId, teamId);
    }

    @GraphQLQuery(name = "GetTotalCardByMatch")
    public List<TotalScorecard> getTotalCardByMatch(
            @GraphQLArgument(name = "matchId") Long matchId
    ) {
        return totalScorecardRepository.findAllByMatchId(
                matchId,
                new Sort(Sort.Direction.ASC, "innings")
        );
    }
}
