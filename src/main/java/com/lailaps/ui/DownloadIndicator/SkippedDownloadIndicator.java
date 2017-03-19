package com.lailaps.ui.DownloadIndicator;



public class SkippedDownloadIndicator extends ProgressDownloadIndicator {

    public SkippedDownloadIndicator(String warning) {
        super(100, warning);
        progressBar.setStyle("-fx-accent: yellow;");
    }
}
