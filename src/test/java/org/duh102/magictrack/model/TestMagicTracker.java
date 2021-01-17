package org.duh102.magictrack.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMagicTracker {

    @Test
    public void testCastSpellBase() {
        MagicTracker tracker = new MagicTracker(MagicLevel.ONE);
        tracker.castSpell(1, 1);
        assertThat(tracker.getCurrentUseLevel()).isEqualTo(1.0);
        tracker.resetTracker().castSpell(1,2);
        assertThat(tracker.getCurrentUseLevel()).isEqualTo(2.0);
    }

    @Test
    public void testCastSpellSmallerDiscountsSmallerCosts() {
        MagicTracker tracker = new MagicTracker(MagicLevel.ONE);
        tracker.castSpell(1, 1);
        assertThat(tracker.getCurrentUseLevel()).isEqualTo(1.0);
        double usedCost = tracker.getCurrentUseLevel();
        tracker.resetTracker().setCharacterLevel(MagicLevel.TWO).castSpell(1, 1);
        assertThat(tracker.getCurrentUseLevel()).isLessThan(1.0);
        assertThat(tracker.getCurrentUseLevel()).isGreaterThan(0.1);
        // The discount should cost less than the normal cost
        assertThat(usedCost).isGreaterThan(tracker.getCurrentUseLevel());
        usedCost = tracker.getCurrentUseLevel();
        tracker.resetTracker().setCharacterLevel(MagicLevel.THREE).castSpell(1, 1);
        assertThat(tracker.getCurrentUseLevel()).isLessThan(1.0);
        assertThat(tracker.getCurrentUseLevel()).isGreaterThan(0.0);
        // And the discount should increase over time
        assertThat(usedCost).isGreaterThan(tracker.getCurrentUseLevel());
    }
}
