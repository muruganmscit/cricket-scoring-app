package com.olikaanoli.scoring.repository;

import com.olikaanoli.scoring.model.TotalScorecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalScorecardRepository extends JpaRepository<TotalScorecard, Long> {

    /**
     * Matching the records based on match id and team id
     */
    TotalScorecard findByMatchIdAndTeamId(Long matchId, Long teamId);
}
