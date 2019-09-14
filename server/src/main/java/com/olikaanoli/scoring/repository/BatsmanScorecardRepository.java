package com.olikaanoli.scoring.repository;

import com.olikaanoli.scoring.model.BatsmanScorecard;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatsmanScorecardRepository extends JpaRepository<BatsmanScorecard, Long> {

    /**
     * Getting the list of batsman for the given team and match
     */
    List<BatsmanScorecard> findAllByMatchIdAndTeamId(Long matchId, Long teamId, Sort sort);

    List<BatsmanScorecard> findAllByMatchIdAndInnings(Long matchId, Integer innings, Sort sort);

    /**
     * Getting data for a particular batsman
     */
    BatsmanScorecard findByBatsman_IdAndMatchId(Long batsmanId, Long matchId);

    /**
     * Getting data for a particular batsman by batting order
     * TODO: Need to check on the logic. if by some mishap if we received
     * 2 records
     */
    BatsmanScorecard findByTeamIdAndMatchIdAndBatting(Long teamID, Long matchId, Integer batting);

    /**
     * Getting the current active batsman for a given match and team
     */
    @Query("select b from BatsmanScorecard b where (b.matchId = ?1 and b.team.id = ?2 and b.batting in (1, 2)) order by b.batsman")
    List<BatsmanScorecard> findAllActiveBatsmanScorecardByMatchIdAndTeamId(Long matchId, Long teamId);

    /**
     * Getting the current active batsman for a given match and innings
     */
    @Query("select b from BatsmanScorecard b where (b.matchId = ?1 and b.innings = ?2 and b.batting in (1, 2)) order by b.batsman")
    List<BatsmanScorecard> findAllActiveBatsmanScorecardByMatchIdAndInnings(Long matchId, Integer innings);

    /**
     * Getting all batsman who are out
     */

    /**
     * Getting all batsman who are not out
     */
    @Query("select b from BatsmanScorecard b where (b.matchId = ?1 and b.team.id = ?2 and b.batting = 3)")
    List<BatsmanScorecard> findAllBatsmanScorecardByMatchIdAndTeamIdAndBatting(Long matchId, Long teamId);

}
