package org.duh102.magictrack;

import org.duh102.magictrack.ui.UIState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main implements ActionListener {
    private UIState state;
    private JFrame frame;

    String title = "Magic Tracker";

    JTextField characterNameField = new JTextField();
    JTextField initialLevelField = new JTextField();
    final JComponent[] newCharacterDialogInputs = new JComponent[] {
            new JLabel("Character Name"),
            characterNameField,
            new JLabel("Initial Level"),
            initialLevelField
    };

    public Main() {
        frame = new JFrame(title);
        state = new UIState(frame, this);
    }
    public void start() {
        frame.setPreferredSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenuItem loadButton = new JMenuItem("Load");
        loadButton.setActionCommand("menu_butLoad");
        loadButton.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        loadButton.addActionListener(this);
        menuBar.add(loadButton);

        JMenuItem saveButton = new JMenuItem("Save");
        saveButton.setActionCommand("menu_butSave");
        saveButton.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveButton.addActionListener(this);
        menuBar.add(saveButton);

        JMenuItem newCharButton = new JMenuItem("New Character");
        newCharButton.setActionCommand("menu_butNewChar");
        newCharButton.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newCharButton.addActionListener(this);
        menuBar.add(newCharButton);

        JMenuItem exitButton = new JMenuItem("Exit");
        exitButton.setActionCommand("menu_butExit");
        exitButton.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exitButton.addActionListener(this);
        menuBar.add(exitButton);

        frame.setJMenuBar(menuBar);

        frame.add(state.getPanel());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        String[] actionComponents = actionCommand.split("_");
        String target = actionComponents[0];
        if(target.equals("menu")) {
            switch(actionComponents[1]) {
                case "butLoad":
                    loadAction();
                    break;
                case "butSave":
                    saveAction();
                    break;
                case "butNewChar":
                    newCharacterAction();
                    break;
                case "butExit":
                    exitAction();
                    break;
            }
        } else {
            state.performAction(actionComponents[1], actionComponents[2]);
        }
        String newTitle = String.format("%s%s", title, state.isStateSaved() ? "" : " (*)");
        frame.setTitle(newTitle);
    }

    public void loadAction() {
        state.loadDialog();
    }
    public void saveAction() {
        state.saveDialog();
    }
    public void exitAction() {
        frame.dispose();
    }
    public void newCharacterAction() {
        int result = JOptionPane.showConfirmDialog(null, newCharacterDialogInputs, String.format("New Character"), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String characterName = characterNameField.getText();
                int level = Integer.parseInt(initialLevelField.getText());
                characterNameField.setText("");
                initialLevelField.setText("");
                state.addCharacter(characterName, level);
            } catch(NumberFormatException nfe) {
                System.err.printf("Bad integer: %s", initialLevelField.getText());
            }
        }
    }
}
