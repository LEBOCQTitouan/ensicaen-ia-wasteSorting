package edu.ensicaen.trashsorting;

import edu.ensicaen.trashsorting.view.WasteSeparationPanel;

import javax.swing.*;
public class TrashSortingApplication {

    public static final int width = 1000;
    public static final int height = 1000;

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Agent-based simulation for waste separation");
        mainFrame.setSize(width, height);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        WasteSeparationPanel panel = new WasteSeparationPanel();
        mainFrame.setContentPane(panel);
        mainFrame.setVisible(true);

        panel.run();
    }

}
