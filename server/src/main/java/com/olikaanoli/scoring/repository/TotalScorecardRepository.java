package com.olikaanoli.scoring.repository;

import com.olikaanoli.scoring.model.TotalScorecard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select t.team.id from TotalScorecard t where (t.team.id <> ?1 and t.matchId = ?2)")
    Long findTeamIdOfCurrentInnings(Long bowlingTeamId, Long matchId);
}
