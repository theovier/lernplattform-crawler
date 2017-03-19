package com.lailaps.ui.DownloadIndicator;


import com.lailaps.download.DownloadableDocument;

public class FailedDownloadIndicator extends ProgressDownloadIndicator {

    public FailedDownloadIndicator(DownloadableDocument failedDownload, Exception cause) {
        super(100, failedDownload);
        progressBar.setStyle("-fx-accent: red;");
    }
}
