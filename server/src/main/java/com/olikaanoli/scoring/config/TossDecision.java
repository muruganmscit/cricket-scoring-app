package com.olikaanoli.scoring.config;

public enum TossDecision {

    BATTING(0),
    FIELDING(1);

    private final int tossDecisionCode;

    TossDecision(int tossDecisionCode) {
        this.tossDecisionCode = tossDecisionCode;
    }

    public int getTossDecisionCode() {
        return this.tossDecisionCode;
    }
}