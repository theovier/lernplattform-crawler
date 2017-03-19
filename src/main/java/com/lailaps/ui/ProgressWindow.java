package com.lailaps.ui;

import com.lailaps.download.DownloadObserver;
import com.lailaps.download.DownloadableDocument;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class ProgressWindow extends Window implements DownloadObserver {

    private static final Logger LOG = Logger.getLogger(ProgressWindow.class);
    private JLabel downloadCounterLabel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private int downloadCounter = 0;
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
        frame.setResizable(true);
        setFonts();
        configureTextArea();
        configureScrollBehaviour();
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        downloadCounterLabel = new JLabel(DEFAULT_INFO, SwingConstants.CENTER);
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
    }

    @Override
    protected void setWidgetPositions() {
        //done by Panel-Layout
    }

    @Override
    protected void initPanel() {
        super.initPanel();
        panel = new JPanel(new BorderLayout(0,10));
        panel.setBorder(new EmptyBorder(10, 10, 20, 10));
    }

    @Override
    protected void addPanelContent() {
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
    public void onDownloadStarted(DownloadableDocument document) {
        downloadCounter++;
        downloadCounterLabel.setText(COUNTER_HEADING + downloadCounter);
        textArea.append(SUCCESS);
        textArea.append(document.toShortString(80));
        textArea.append(System.lineSeparator());
        LOG.info("Downloaded: " + document);
    }

    @Override
    public void onDownloadSkipped(DownloadableDocument skippedDocument) {
        textArea.append(SKIPPED);
        textArea.append(skippedDocument.toShortString(60));
        textArea.append(System.lineSeparator());
    }

    @Override
    public void onDownloadFinished() {
        frame.setTitle(TITLE_FINISHED);
        downloadCounterLabel.setText(COUNTER_FINISHED + downloadCounter);
        for (int i = 0; i < 60; i++) {
            textArea.append("=");
        }
    }

    @Override
    public void onDownloadFailed(DownloadableDocument failedDocument, Exception cause) {

    }

    @Override
    public void onDownloadProgress(DownloadableDocument document, double currentProgress) {

    }
}
