package com.olikaanoli.scoring.repository;

import com.aexp.samplespqr.model.Ball;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallsRepository extends JpaRepository<Ball, Long> {
}
