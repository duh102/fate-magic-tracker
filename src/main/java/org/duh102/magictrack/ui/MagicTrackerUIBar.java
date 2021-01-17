package org.duh102.magictrack.ui;

import org.duh102.magictrack.model.MagicTracker;

import javax.swing.*;
import java.awt.*;

public class MagicTrackerUIBar {
    private MagicTracker tracker;
    private JProgressBar magicBarNormal, magicBarOver;

    private static final double maxOutAmount = 1.5;

    private int convertToProgBarInts(double amount) {
        return (int)Math.round(amount*100);
    }
    private int calcMaxOut(double safeUseLevel) {
        return convertToProgBarInts(safeUseLevel*maxOutAmount);
    }

    public MagicTrackerUIBar(MagicTracker tracker) {
        this.tracker = tracker;

        magicBarNormal = new JProgressBar();
        magicBarNormal.setStringPainted(true);

        magicBarOver = new JProgressBar();
        magicBarOver.setForeground(Color.RED);
        magicBarOver.setString(" ");
        magicBarOver.setStringPainted(true);
        recalcLimits();
        recalcBarFill();
    }

    public MagicTrackerUIBar castSpell(int level, int cost) {
        tracker.castSpell(level, cost);
        recalcBarFill();
        return this;
    }

    public MagicTracker getTracker() {
        return tracker;
    }
    public MagicTrackerUIBar recalcLimits() {
        int safeIntervalMax = convertToProgBarInts(tracker.getMaxSafeUseLevel());
        int unsafeMax = calcMaxOut(tracker.getMaxSafeUseLevel());
        magicBarNormal.setMaximum(safeIntervalMax);
        magicBarOver.setMinimum(safeIntervalMax);
        magicBarOver.setMaximum(unsafeMax);
        return this;
    }
    public MagicTrackerUIBar recalcBarFill() {
        int currentLevel = convertToProgBarInts(tracker.getCurrentUseLevel());
        magicBarNormal.setValue(Math.min(magicBarNormal.getMaximum(), Math.max(magicBarNormal.getMinimum(), currentLevel)));
        magicBarNormal.setString(String.format("%.2f/%.0f", currentLevel/100.0, magicBarNormal.getMaximum()/100.0));
        magicBarOver.setValue(Math.min(magicBarOver.getMaximum(), Math.max(magicBarOver.getMinimum(), currentLevel)));
        return this;
    }
    public JProgressBar getMagicBarNormal() {
        return magicBarNormal;
    }
    public JProgressBar getMagicBarOver() {
        return magicBarOver;
    }
}
