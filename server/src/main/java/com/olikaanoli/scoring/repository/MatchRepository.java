package com.olikaanoli.scoring.repository;

import com.olikaanoli.scoring.config.InningStatus;
import com.olikaanoli.scoring.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("select m.currentInnings from Match m where (m.id = ?1)")
    InningStatus findInningsByMatchID(Long matchId);
}
