package org.duh102.magictrack.model.serialization;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.duh102.magictrack.model.Character;
import org.duh102.magictrack.model.MagicTracker;

public class CharacterDeserializationFixup extends StdConverter<Character, Character> {
    @Override
    public Character convert(Character character) {
        MagicTracker tracker = character.getCurrentMagicState();
        if(character.getLevel() != tracker.getCharacterLevel()) {
            tracker.setCharacterLevel(character.getLevel());
        }
        return character;
    }
}
