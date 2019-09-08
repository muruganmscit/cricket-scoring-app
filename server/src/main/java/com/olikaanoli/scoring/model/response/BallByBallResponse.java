package com.olikaanoli.scoring.model.response;

import com.olikaanoli.scoring.model.Ball;
import com.olikaanoli.scoring.model.BatsmanScorecard;
import com.olikaanoli.scoring.model.BowlerScorecard;
import com.olikaanoli.scoring.model.TotalScorecard;
import lombok.Data;

import java.util.List;

@Data
public class BallByBallResponse {

    private List<TotalScorecard> totalScorecards;

    // current bowler details
    private BowlerScorecard bowlerScorecard;

    // list of current over balls
    private List<Ball> balls;

    // current batsman
    private List<BatsmanScorecard> batsmanScorecards;
}
