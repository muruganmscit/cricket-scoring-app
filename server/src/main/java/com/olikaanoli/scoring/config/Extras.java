package com.olikaanoli.scoring.config;

/**
 * Enum to hold all the types of extras that we are dealing witha
 */
public enum Extras {
    BYES(0),
    LEG_BYES(1),
    NO_BALL(2),
    PENALTY(3),
    WIDE(4),
    WIDE_BYES(5),
    NO_BALL_BYES(6),
    NO_BALL_LEG_BYES(7);

    private final int extrasCode;

    /**
     * Constructor
     * @param extrasCode
     */
    Extras(int extrasCode) {
        this.extrasCode = extrasCode;
    }

    /**
     * Getter method for the code
     * @return
     */
    public int getExtrasCode() {
        return this.extrasCode;
    }
}
