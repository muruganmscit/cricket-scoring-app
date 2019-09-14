package com.olikaanoli.scoring.model.response;

import com.olikaanoli.scoring.model.*;
import lombok.Data;

import java.util.List;

@Data
public class BallByBallResponse {

    private Match match;

    private List<TotalScorecard> totalScorecards;

    // current bowler details
    private BowlerScorecard bowlerScorecard;

    // list of current over balls
    private List<Ball> balls;

    // current batsman
    private List<BatsmanScorecard> batsmanScorecards;

    // current innings all batsman
    private List<BatsmanScorecard> playing11Innings1;

    // current innings all bowler
    private List<BatsmanScorecard> playing11Innings2;
}
