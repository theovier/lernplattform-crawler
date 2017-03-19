package com.lailaps.ui.DownloadIndicator;


public class FailedDownloadIndicator extends ProgressDownloadIndicator {

    public FailedDownloadIndicator(String error) {
        super(100, error);
        //color red
    }
}
