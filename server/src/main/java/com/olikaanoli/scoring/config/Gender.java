package com.olikaanoli.scoring.config;

public enum Gender {

    MALE(0),
    FEMALE(1),
    OTHERS(2),
    NOT_APPLICABLE(3);

    private final int genderCode;

    /**
     * Constructor
     * @param genderCode
     */
    Gender(int genderCode) {
        this.genderCode = genderCode;
    }

    /**
     * Getter method for the code
     * @return
     */
    public int getGenderCode() {
        return this.genderCode;
    }
}
