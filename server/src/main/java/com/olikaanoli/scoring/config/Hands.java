package com.olikaanoli.scoring.config;

public enum Hands {

    RIGHT_HAND(0),
    LEFT_HAND(1);

    private final int handCode;

    /**
     * Constructor
     */
    Hands(int handCode) {
        this.handCode = handCode;
    }

    public int getHandCode() {
        return handCode;
    }
}
