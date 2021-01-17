package org.duh102.magictrack.ui;

import org.duh102.magictrack.model.Character;
import org.duh102.magictrack.model.MagicLevel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class UIState {
    private JPanel panel;
    private Map<Character, CharacterPanel> characterPanels;
    private Map<String, Character> characterMap;
    private ActionListener listener;
    private boolean stateSaved = false;

    private JFrame container;

    JTextField spellCostField = new JTextField();
    JTextField spellLevelField = new JTextField();
    final JComponent[] spellDialogInputs = new JComponent[] {
            new JLabel("Spell cost"),
            spellCostField,
            new JLabel("Spell level"),
            spellLevelField
    };


    public UIState(JFrame container, ActionListener actionListener) {
        panel = new JPanel();
        this.container = container;
        this.listener = actionListener;
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        characterPanels = new HashMap<>();
    }
    public UIState addCharacter(String charName, int initialLevel) {
        initialLevel = Math.min(Math.max(0, initialLevel-1), MagicLevel.MAGIC_LEVELS.length-1);
        Character character = new Character(charName, MagicLevel.MAGIC_LEVELS[initialLevel]);
        CharacterPanel newCharacterPanel = new CharacterPanel(panel, listener, character);
        characterPanels.put(character, newCharacterPanel);
        regenCharNameMap();

        panel.add(newCharacterPanel.getPanel());

        stateSaved = false;
        panel.repaint();
        panel.revalidate();
        container.revalidate();
        return this;
    }
    public UIState removeCharacter(String charName) {
        Character toRemove = characterMap.remove(charName);
        if( toRemove == null ) {
            return this;
        }
        CharacterPanel toRemovePanel = characterPanels.remove(toRemove);
        regenCharNameMap();

        panel.remove(toRemovePanel.getPanel());

        stateSaved = false;
        panel.repaint();
        panel.revalidate();
        container.revalidate();
        return this;
    }

    public JPanel getPanel() {
        return panel;
    }
    private void regenCharNameMap() {
        characterMap = new HashMap<>();
        for(Character character : characterPanels.keySet()) {
            characterMap.put(character.getName(), character);
        }
    }

    public void performAction(String characterName, String buttonName) {
        if(!characterMap.containsKey(characterName)) {
            System.err.printf("No character registered with name '%s'\n", characterName);
            return;
        }
        Character character = characterMap.get(characterName);
        CharacterPanel charPanel = characterPanels.get(character);
        switch(buttonName) {
            case "butCast":
                castSpellDialog(character, charPanel);
                break;
            case "butUp":
                charPanel.levelUp();
                break;
            case "butDown":
                charPanel.levelDown();
                break;
            case "butReset":
                charPanel.reset();
                break;
        }
    }

    public UIState setSaved() {
        stateSaved = true;
        return this;
    }
    public boolean isStateSaved() {
        return stateSaved;
    }

    public void castSpellDialog(Character character, CharacterPanel toCastWith) {
        int result = JOptionPane.showConfirmDialog(null, spellDialogInputs, String.format("Cast Spell - %s", character.getName()), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int level = Integer.parseInt(spellLevelField.getText());
                int cost = Integer.parseInt(spellCostField.getText());
                spellLevelField.setText("");
                spellCostField.setText("");
                toCastWith.castSpell(level, cost);
            } catch(NumberFormatException nfe) {
                System.err.printf("Bad integer(s): %s, %s", spellCostField.getText(), spellLevelField.getText());
            }
        }
    }
}
