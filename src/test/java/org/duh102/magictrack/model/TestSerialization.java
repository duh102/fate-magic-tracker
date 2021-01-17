package org.duh102.magictrack.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestSerialization {
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testMagicTracker() throws JsonProcessingException {
        MagicLevel level = MagicLevel.THREE;
        MagicTracker a = new MagicTracker(level);
        a.castSpell(level.getLevelInt(), 3);
        assertThat(a.getCurrentUseLevel()).isEqualTo(3.0);

        String json = mapper.writeValueAsString(a);

        MagicTracker b = mapper.readValue(json, MagicTracker.class);
        assertThat(b.getCurrentUseLevel()).isEqualTo(3.0);
        assertThat(b.getMaxSafeUseLevel()).isEqualTo(level.getMaxSafe());
        assertThat(b.getCharacterLevel().getLevelInt()).isEqualTo(level.getLevelInt());
    }
}
