package org.duh102.magictrack.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.atomicIntegerFieldUpdater;

public class TestSerialization {
    private ObjectMapper mapper = new ObjectMapper();

    // Serialization of the MagicTracker relies on the character, so alone it won't have level information
    @Test
    public void testMagicTracker() throws JsonProcessingException {
        MagicLevel level = MagicLevel.THREE;
        MagicTracker a = new MagicTracker(level);
        a.castSpell(level.getLevelInt(), 3);
        assertThat(a.getCurrentUseLevel()).isEqualTo(3.0);

        String json = mapper.writeValueAsString(a);

        MagicTracker b = mapper.readValue(json, MagicTracker.class);
        assertThat(b.getCurrentUseLevel()).isEqualTo(3.0);
        assertThat(b.getCharacterLevel()).isEqualTo(MagicLevel.ONE);
    }

    @Test
    public void testCharacter() throws JsonProcessingException {
        Character c = new Character("c", MagicLevel.THREE);
        assertThat(c.getCurrentMagicState().getCurrentUseLevel()).isEqualTo(0.0);
        c.castSpell(5, 1);
        assertThat(c.getCurrentMagicState().getCurrentUseLevel()).isBetween(0.1, 5.0);

        String json = mapper.writeValueAsString(c);

        Character cImport = mapper.readValue(json, Character.class);
        assertThat(cImport.getName()).isEqualTo(c.getName());
        assertThat(cImport.getLevel()).isEqualTo(c.getLevel());
        MagicTracker cMag = c.getCurrentMagicState();
        MagicTracker cImpMag = cImport.getCurrentMagicState();
        assertThat(cMag.getCurrentUseLevel()).isEqualTo(cImpMag.getCurrentUseLevel());
        assertThat(cMag.getCharacterLevel()).isEqualTo(cImpMag.getCharacterLevel());
        assertThat(cMag.getMaxSafeUseLevel()).isEqualTo(cImpMag.getMaxSafeUseLevel());
    }
}
