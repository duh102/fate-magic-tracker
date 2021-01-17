package org.duh102.magictrack.model;

public enum MagicLevel {
    ONE(1, 6, 0),
    TWO(2, 8, 0.1),
    THREE(3, 10, 0.2),
    FOUR(4, 14, 0.35),
    FIVE(5, 20, 0.5);

    public static final MagicLevel[] MAGIC_LEVELS = {
            ONE, TWO, THREE, FOUR, FIVE
    };
    public static final MagicLevel MIN_LEVEL = MAGIC_LEVELS[0];
    public static final MagicLevel MAX_LEVEL = MAGIC_LEVELS[MAGIC_LEVELS.length-1];


    private int levelDisplay;
    private double maxSafe;
    private double discount;

    MagicLevel(int levelDisplay, double maxSafe, double discount) {
        this.levelDisplay = levelDisplay;
        this.maxSafe = maxSafe;
        this.discount = discount;
    }

    public int getLevelInt() {
        return levelDisplay;
    }
    public double getMaxSafe() {
        return maxSafe;
    }
    public double getDiscount() {
        return discount;
    }
}
