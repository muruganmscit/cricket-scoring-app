package com.olikaanoli.scoring.repository;

import com.olikaanoli.scoring.model.Ball;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BallsRepository extends JpaRepository<Ball, Long> {

    /**
     * Getting the list of balls in the over ordered for the match and team
     */
    List<Ball> findAllBallsByMatchIdAndTeamIdAndOvers(Long matchId, Long teamId, int overs, Sort sort);

    List<Ball> findAllBallsByMatchIdAndInningsAndOvers(Long matchId, Integer innings, int overs, Sort sort);

    /**
     * Getting the current bowler for a match
     * TODO: Need to check on the logic. if by some mishap if we received
     * 2 records
     */
    @Query("select max(i.overs) from Ball i where (i.matchId = ?1 and i.teamId = ?2)")
    Integer findRunningOverForMatchIdAndTeam(Long matchId, Long teamId);

    /**
     * Getting the current bowler for a match and innings
     * TODO: Need to check on the logic. if by some mishap if we received
     * 2 records
     */
    @Query("select max(i.overs) from Ball i where (i.matchId = ?1 and i.innings = ?2)")
    Integer findRunningOverForMatchIdAndInnings(Long matchId, Integer innings);

}
