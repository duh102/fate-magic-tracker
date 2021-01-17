package org.duh102.magictrack.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.duh102.magictrack.model.Character;
import org.duh102.magictrack.model.MagicLevel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        return addCharacterInner(character, false);
    }
    public UIState addCharacterInner(Character character, boolean batchAdd) {
        CharacterPanel newCharacterPanel = new CharacterPanel(panel, listener, character);
        characterPanels.put(character, newCharacterPanel);
        if(!batchAdd) {
            regenCharNameMap();
        }

        panel.add(newCharacterPanel.getPanel());

        setModified();
        if(!batchAdd) {
            refreshView();
        }
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
        refreshView();
        setModified();
        return this;
    }

    private void refreshView() {
        panel.repaint();
        panel.revalidate();
        container.revalidate();
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
        setModified();
    }

    public UIState setSaved() {
        stateSaved = true;
        return this;
    }
    public UIState setModified() {
        stateSaved = false;
        return this;
    }
    public boolean isStateSaved() {
        return stateSaved;
    }

    public UIState loadDialog() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setSelectedFile(new File("magicstate.json"));
        int retVal = fc.showOpenDialog(panel);
        if(retVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            ObjectMapper mapper = new ObjectMapper();
            List<Character> characters = null;
            try {
                characters = mapper.readValue(selectedFile, new TypeReference<List<Character>>(){});
            } catch (IOException e) {
                System.err.println("Error while loading:");
                e.printStackTrace();
            }
            characterPanels.clear();
            panel.removeAll();
            if(characters != null) {
                for (Character character : characters) {
                    addCharacterInner(character, true);
                }
            }
            regenCharNameMap();
            refreshView();
            setSaved();
        }
        return this;
    }

    public UIState saveDialog() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setSelectedFile(new File("magicstate.json"));
        int retVal = fc.showSaveDialog(panel);
        if(retVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            ObjectMapper mapper = new ObjectMapper();
            List<Character> characters = new ArrayList<>(characterPanels.keySet());
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(selectedFile, characters);
                setSaved();
            } catch (IOException e) {
                System.err.println("Error while saving:");
                e.printStackTrace();
            }
        }
        return this;
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
