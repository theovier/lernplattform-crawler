package com.lailaps.ui;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

//todo extract baseclass WINDOW
public class ProgressWindow {

    private JFrame frame;
    private JPanel panel;
    private JLabel infoLabel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    
    public ProgressWindow() {
        setLookAndFeel();
        initWidgets();
        configureTextArea();
        initPanel();
        addPanelContent();
        initFrame();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            //won't happen
        }
    }

    private void initWidgets() {
        infoLabel = new JLabel("Please wait...", SwingConstants.CENTER);
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
    }

    private void initPanel() {
        panel = new JPanel(new BorderLayout(0,10));
        panel.setBorder(new EmptyBorder(10, 10, 20, 10));
    }

    private void configureTextArea() {
        textArea.setEditable(false);
    }

    private void addPanelContent() {
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(textArea, BorderLayout.CENTER);
    }

    private void initFrame() {
        frame = new JFrame("Downloading...");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //todo hide on close? and button to show again?
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
    }

    public void show() {
        frame.setVisible(true);
    }
}
