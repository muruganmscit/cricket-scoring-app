package com.olikaanoli.scoring.input.balls;

import com.olikaanoli.scoring.config.Extras;
import com.olikaanoli.scoring.config.Wickets;
import lombok.Data;

@Data
public class BallInput {

    // it can be either 1 / 2
    private int innings;

    // unique id for the match
    private int matchId;

    // team that plays batting
    private int teamId;

    // over that is currently running
    private int overs;

    // current ball
    private int ball;

    // player facing the ball
    private int batsman;

    // player who delivers the ball
    private int bowler;

    // player who is not facing the ball
    private int nonStriker;

    // type of extras if any. this should come from a enum
    private Extras extraType = null;

    // Total runs scored in the ball
    private int runsTotal = 0;

    // if wicket, what type it is
    private Wickets wicketType = null;

    // player who got out
    private Integer wicketPlayer = null;

    // player who is involved in the wicket
    private Integer fielder1 = null;

    // player who is involved in the wicket
    private Integer fielder2 = null;

    // if the runs scored came by boundary
    private Boolean nonBoundary = false;

}
