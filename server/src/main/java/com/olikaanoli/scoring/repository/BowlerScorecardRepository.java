package com.olikaanoli.scoring.repository;

import com.olikaanoli.scoring.model.BowlerScorecard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BowlerScorecardRepository extends JpaRepository<BowlerScorecard, Long> {

    /**
     * Getting the list of bowlers for the given team and match
     */
    List<BowlerScorecard> findAllBowlersByMatchIdAndTeamId(Long matchId, Long teamId, Sort sort);

    /**
     * Getting data for a particular bowler
     */
    BowlerScorecard findByBowler_IdAndMatchId(Long bowlerId, Long matchId);

    /**
     * Getting the current bowler for a match and team
     * TODO: Need to check on the logic. if by some mishap if we received
     * 2 records
     */
    @Query("select b from BowlerScorecard b where (b.team.id = ?1 and b.matchId = ?2 and b.bowling = true)")
    BowlerScorecard findCurrentBlowerByTeamIdAndMatchIdAndBatting(Long teamID, Long matchId);


}
