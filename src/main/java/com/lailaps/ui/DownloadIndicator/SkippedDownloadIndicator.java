package com.lailaps.ui.DownloadIndicator;


import com.lailaps.download.DownloadableDocument;

import java.util.ResourceBundle;

public class SkippedDownloadIndicator extends ProgressDownloadIndicator {

    public SkippedDownloadIndicator(DownloadableDocument skippedDocument, ResourceBundle bundle) {
        super(skippedDocument, 1);
        progressBar.setStyle("-fx-accent: yellow;");
        String message = bundle.getString("downloadScreen.skippedMSG") + " " + skippedDocument.toString();
        setText(message);
    }
}
