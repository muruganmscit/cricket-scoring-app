package com.olikaanoli.scoring.repository;

import com.olikaanoli.scoring.model.TotalScorecard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TotalScorecardRepository extends JpaRepository<TotalScorecard, Long> {

    /**
     * Matching the records based on match id
     */
    TotalScorecard findByMatchIdAndTeamId(Long matchId, Long teamId);

    /**
     * Matching the records based on match id and team id
     */
    List<TotalScorecard> findAllByMatchId(Long matchId, Sort sort);
}
