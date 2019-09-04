package com.olikaanoli.scoring.config;

public enum Wickets {

    BOWLED(0, "Bowled"),
    CAUGHT(1, "Caught"),
    CAUGHT_AND_BOWLED(2, "Caught and Bouwled"),
    LBW(3, "LBW"),
    STUMPED(4, "Stumped"),
    RUN_OUT(5, "Run Out"),
    RETIRED_HURT(6, "Retired Hurt"),
    HIT_WICKET(7, "Hit Wicket"),
    OBSTRUCTING_THE_FIELD(8, "Obstructing the Field"),
    HIT_THE_BALL_TWICE(9, "Hit the Ball Twice"),
    HANDLED_THE_BALL(10, "Handling the Ball"),
    TIMED_OUT(11, "Timed Out");

    private final int wicketCode;
    private final String wicketName;

    Wickets(int wicketCode, String wicketName) {
        this.wicketCode = wicketCode;
        this.wicketName = wicketName;
    }

    /**
     * Getting the wicket code
     * @return int Wicket Code
     */
    public int getWicketCode() {
        return wicketCode;
    }

    /**
     * Getting the Wicket name
     * @return String Wicket Name
     */
    public String getWicketName() {
        return wicketName;
    }
}

