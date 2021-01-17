package org.duh102.magictrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MagicTracker {
    MagicLevel characterLevel;
    double currentUseLevel;

    public MagicTracker() {
        this(MagicLevel.ONE);
    }
    public MagicTracker(MagicLevel level) {
        if(level == null) {
            level = MagicLevel.ONE;
        }
        characterLevel = level;
        currentUseLevel = 0;
    }

    public MagicTracker castSpell(int level, int cost) {
        currentUseLevel += Math.max(cost - (cost * (characterLevel.getLevelInt() - level) * characterLevel.getDiscount()), 0.1);
        return this;
    }

    public MagicTracker resetTracker() {
        currentUseLevel = 0;
        return this;
    }
    public MagicTracker setCharacterLevel(MagicLevel characterLevel) {
        this.characterLevel = characterLevel;
        return this;
    }
    @JsonIgnore
    public MagicLevel getCharacterLevel() {
        return characterLevel;
    }
    public double getCurrentUseLevel() {
        return currentUseLevel;
    }
    @JsonIgnore
    public double getMaxSafeUseLevel() {
        return characterLevel.getMaxSafe();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MagicTracker that = (MagicTracker) o;
        return Double.compare(that.getCurrentUseLevel(), getCurrentUseLevel()) == 0 && Double.compare(that.getMaxSafeUseLevel(), getMaxSafeUseLevel()) == 0 && getCharacterLevel() == that.getCharacterLevel();
    }
}
