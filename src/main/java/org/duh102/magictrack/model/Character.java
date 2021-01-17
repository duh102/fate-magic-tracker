package org.duh102.magictrack.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.duh102.magictrack.model.serialization.CharacterDeserializationFixup;

import java.util.Objects;

@JsonDeserialize(converter=CharacterDeserializationFixup.class)
public class Character {
    String name;
    MagicTracker currentMagicState;
    MagicLevel level;

    public Character() {
        this("", MagicLevel.ONE);
    }
    public Character(String name, MagicLevel level) {
        this.name = name;
        this.level = level;
        currentMagicState = new MagicTracker(level);
    }
    public Character(String name, MagicLevel level, MagicTracker tracker) {
        this.name = name;
        this.level = level;
        this.currentMagicState = tracker.setCharacterLevel(level);
    }

    public Character castSpell(int level, int cost) {
        currentMagicState.castSpell(level, cost);
        return this;
    }

    public Character setName(String name) {
        this.name = name;
        return this;
    }
    public Character setCurrentMagicState(MagicTracker currentMagicState) {
        this.currentMagicState = currentMagicState;
        return this;
    }
    public Character levelUp() {
        if(level != MagicLevel.MAX_LEVEL) {
            level = MagicLevel.MAGIC_LEVELS[level.getLevelInt()];
        }
        currentMagicState.setCharacterLevel(level);
        return this;
    }
    public Character levelDown() {
        if(level != MagicLevel.MIN_LEVEL) {
            level = MagicLevel.MAGIC_LEVELS[level.getLevelInt()-2];
        }
        currentMagicState.setCharacterLevel(level);
        return this;
    }
    public Character reset() {
        currentMagicState.resetTracker();
        return this;
    }

    public String getName() {
        return name;
    }
    public MagicLevel getLevel() {
        return level;
    }
    public MagicTracker getCurrentMagicState() {
        return currentMagicState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return getName().equals(character.getName()) && getCurrentMagicState().equals(character.getCurrentMagicState()) && getLevel() == character.getLevel();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
