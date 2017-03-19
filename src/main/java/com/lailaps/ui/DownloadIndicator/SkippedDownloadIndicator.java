package com.lailaps.ui.DownloadIndicator;


import com.lailaps.download.DownloadableDocument;

public class SkippedDownloadIndicator extends ProgressDownloadIndicator {

    private static final String SKIPPED_MSG = "File already exists (skipped): ";

    public SkippedDownloadIndicator(DownloadableDocument skippedDocument) {
        super(100, skippedDocument);
        progressBar.setStyle("-fx-accent: yellow;");
        String message = SKIPPED_MSG + " " + skippedDocument.toString();
        setText(message);
    }
}