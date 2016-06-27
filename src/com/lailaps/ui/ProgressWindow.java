package com.lailaps.ui;

import com.lailaps.download.DownloadObserver;
import com.lailaps.download.DownloadableDocument;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;

//todo stop button.
public class ProgressWindow extends Window implements DownloadObserver {

    private JLabel infoLabel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private int downloadCounter;
    private static final String DEFAULT_INFO = "Please wait...";

    public ProgressWindow() {
        super(600, 400, "Downloading...");
        downloadCounter = 0;
        configureTextArea();
    }

    protected void initWidgets() {
        super.initWidgets();
        infoLabel = new JLabel(DEFAULT_INFO, SwingConstants.CENTER);
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
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private void configureTextArea() {
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    @Override
    public void addDownload(DownloadableDocument document) {
        downloadCounter++;
        infoLabel.setText("Files downloaded: #" + downloadCounter);
        textArea.append(document.toString());
        textArea.append(System.lineSeparator());
    }

    @Override
    public void skippedDownload(DownloadableDocument skippedDocument) {
        textArea.append("File already exists (skipped): ");
        textArea.append(skippedDocument.toString());
        textArea.append(System.lineSeparator());
    }

    @Override
    public void finishedDownloading() {
        frame.setTitle("Finished Downloading");
        infoLabel.setText("finished. Downloaded: #" + downloadCounter);
        for (int i = 0; i < 50; i++) {
            textArea.append("=");
        }
    }
}
