package com.lailaps.ui;

import com.lailaps.download.DownloadObserver;
import com.lailaps.download.DownloadableDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class ProgressWindow extends Window implements DownloadObserver {

    private JLabel downloadCounterLabel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private int downloadCounter;
    private Font defaultFont;
    private static final String DEFAULT_INFO = "Please wait...";
    private static final String TITLE = "Downloading...";
    private static final String TITLE_FINISHED = "Finished Downloading";
    private static final String COUNTER_HEADING = "Files downloaded #";
    private static final String COUNTER_FINISHED = "finished. Downloaded: #";
    private static final String SUCCESS = "Downloaded: ";
    private static final String SKIPPED = "File already exists (skipped): ";

    public ProgressWindow() {
        super(600, 400, TITLE);
        downloadCounter = 0;
        frame.setResizable(true);
        setFonts();
        configureTextArea();
        configureScrollBehaviour();
    }

    protected void initWidgets() {
        super.initWidgets();
        downloadCounterLabel = new JLabel(DEFAULT_INFO, SwingConstants.CENTER);
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
    }

    protected void initPanel() {
        super.initPanel();
        panel = new JPanel(new BorderLayout(0,10));
        panel.setBorder(new EmptyBorder(10, 10, 20, 10));
    }

    protected void addPanelContent() {
        super.addPanelContent();
        panel.add(downloadCounterLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private void setFonts() {
        defaultFont = UIManager.getDefaults().getFont("TextPane.font");
    }

    private void configureTextArea() {
        textArea.setEditable(false);
        textArea.setFont(defaultFont);
    }

    private void configureScrollBehaviour() {
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    @Override
    public void addDownload(DownloadableDocument document) {
        downloadCounter++;
        downloadCounterLabel.setText(COUNTER_HEADING + downloadCounter);
        textArea.append(SUCCESS);
        textArea.append(document.toShortString(80));
        textArea.append(System.lineSeparator());
    }

    @Override
    public void skippedDownload(DownloadableDocument skippedDocument, boolean isError) {
        textArea.append(SKIPPED);
        textArea.append(skippedDocument.toShortString(60));
        textArea.append(System.lineSeparator());
    }

    @Override
    public void finishedDownloading() {
        frame.setTitle(TITLE_FINISHED);
        downloadCounterLabel.setText(COUNTER_FINISHED + downloadCounter);
        for (int i = 0; i < 60; i++) {
            textArea.append("=");
        }
    }
}
