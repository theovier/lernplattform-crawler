package com.lailaps.ui.DownloadIndicator;



public class SkippedDownloadIndicator extends ProgressDownloadIndicator {

    public SkippedDownloadIndicator(String text) {
        super(100, text);
        progressBar.setStyle("-fx-accent: yellow;");
    }
}
