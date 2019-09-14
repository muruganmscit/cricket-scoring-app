package com.olikaanoli.scoring.config;

public enum InningStatus {

    TEAM_SELECTED(0),
    FIRST_INNINGS(1),
    SECOND_INNINGS(2),
    INNINGS_BREAK(3),
    MATCH_INIT(4),
    MATCH_END(5);

    private final int inningsStatusCode;

    /**
     * Constructor
     * @param inningsStatusCode
     */
    InningStatus(int inningsStatusCode) {
        this.inningsStatusCode = inningsStatusCode;
    }

    public int getInningsStatusCode() {
        return inningsStatusCode;
    }
}
