package org.duh102.magictrack.ui;

import org.duh102.magictrack.model.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CharacterPanel {
    private final JPanel container;
    private final JPanel panel;
    private final Character character;
    private final JLabel charLevelLabel;
    private final MagicTrackerUIBar magicBar;

    public CharacterPanel(JPanel container, ActionListener listener, Character character) {
        this.container = container;
        this.character = character;

        panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, 30));
        panel.setMinimumSize(new Dimension(300,20));
        panel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        JLabel charNameLabel = new JLabel(generateCharacterLabel());
        charLevelLabel = new JLabel(generateCharacterLevelLabel());
        magicBar = new MagicTrackerUIBar(character.getCurrentMagicState());

        JButton castSpellButton = new JButton("Cast");
        castSpellButton.setActionCommand(String.format("char_%s_butCast", character.getName()));
        castSpellButton.addActionListener(listener);

        JButton levelUpButton = new JButton("+");
        levelUpButton.setMargin(new Insets(1,1,1,1));
        levelUpButton.setActionCommand(String.format("char_%s_butUp", character.getName()));
        levelUpButton.addActionListener(listener);

        JButton levelDownButton = new JButton("-");
        levelDownButton.setMargin(new Insets(1,1,1,1));
        levelDownButton.setActionCommand(String.format("char_%s_butDown", character.getName()));
        levelDownButton.addActionListener(listener);

        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand(String.format("char_%s_butReset", character.getName()));
        resetButton.addActionListener(listener);

        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 2;
        // First we put in all the stuff that occupies 2 rows; all the "centered" stuff
        c.gridx = 0;
        c.insets = new Insets(5,5,5,5);
        panel.add(charNameLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridwidth = 4;
        c.insets = new Insets(5,0,5,0);
        panel.add(magicBar.getMagicBarNormal(), c);
        c.gridx = 7;
        panel.add(magicBar.getMagicBarOver(), c);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 11;
        c.gridwidth = 1;
        panel.add(castSpellButton, c);
        c.gridx = 12;
        panel.add(resetButton, c);

        c.gridx = 1;
        panel.add(charLevelLabel, c);

        // Now anything that is only 1 row high
        c.gridheight = 1;
        c.gridx = 2;
        c.gridy = 0;
        panel.add(levelUpButton, c);
        c.gridy = 1;
        panel.add(levelDownButton, c);

        container.add(panel);
    }

    public JPanel getPanel() {
        return panel;
    }

    public CharacterPanel levelUp() {
        character.levelUp();
        charLevelLabel.setText(generateCharacterLevelLabel());
        magicBar.recalcLimits().recalcBarFill();
        refreshPanels();
        return this;
    }
    public CharacterPanel levelDown() {
        character.levelDown();
        charLevelLabel.setText(generateCharacterLevelLabel());
        magicBar.recalcLimits().recalcBarFill();
        refreshPanels();
        return this;
    }
    public CharacterPanel castSpell(int level, int cost) {
        character.castSpell(level, cost);
        magicBar.recalcBarFill();
        refreshPanels();
        return this;
    }
    public CharacterPanel reset() {
        character.reset();
        magicBar.recalcBarFill();
        refreshPanels();
        return this;
    }

    private void refreshPanels() {
        panel.repaint();
        panel.revalidate();
        container.revalidate();
    }

    private String generateCharacterLabel() {
        return character.getName();
    }
    private String generateCharacterLevelLabel() {
        return String.format("Level %d", character.getLevel().getLevelInt());
    }
}
