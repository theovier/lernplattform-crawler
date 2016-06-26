package com.lailaps.ui;


import com.lailaps.download.DownloadObserver;
import com.lailaps.download.DownloadableDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;

//todo extract baseclass WINDOW
public class ProgressWindow implements DownloadObserver {

    private JFrame frame;
    private JPanel panel;
    private JLabel infoLabel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private int downloadCounter;
    private static final String DEFAULT_INFO = "Please wait...";

    public ProgressWindow() {
        setLookAndFeel();
        initWidgets();
        configureTextArea();
        initPanel();
        addPanelContent();
        initFrame();
        downloadCounter = 0;
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
        infoLabel = new JLabel(DEFAULT_INFO, SwingConstants.CENTER);
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
    }

    private void initPanel() {
        panel = new JPanel(new BorderLayout(0,10));
        panel.setBorder(new EmptyBorder(10, 10, 20, 10));
    }

    private void configureTextArea() {
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    private void addPanelContent() {
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private void initFrame() {
        frame = new JFrame("Downloading...");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
    }

    public void show() {
        frame.setVisible(true);
    }

    @Override
    public void addDownload(DownloadableDocument document) {
        if (document == null) {
         //todo change this
            frame.setTitle("Finished downloading");
            infoLabel.setText("finished. Downloaded: #" + downloadCounter);
            textArea.append("====================================================" + System.lineSeparator());
            textArea.append("FINISHED DOWNLOADING");
        } else {
            downloadCounter++;
            infoLabel.setText("Files downloaded: #" + downloadCounter);
            textArea.append(document.toString() +  System.lineSeparator());
        }
    }
}
