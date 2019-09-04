package com.olikaanoli.scoring.config;

public enum PlayerRoles {
    BATSMAN(0),
    BOWLER(1),
    ALL_ROUNDER(2),
    WICKETKEEPER(3);

    private final int playerRoleCode;

    /**
     * Constructor
     */
    PlayerRoles(int playerRoleCode) {
        this.playerRoleCode = playerRoleCode;
    }

    public int getPlayerRoleCode() {
        return this.playerRoleCode;
    }
}
