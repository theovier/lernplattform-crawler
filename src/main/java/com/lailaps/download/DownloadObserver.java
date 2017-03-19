package com.lailaps.download;

public interface DownloadObserver {
    void onDownloadStarted(DownloadableDocument document);
    void onDownloadProgress(DownloadableDocument document, double currentProgress);
    void onDownloadSkipped(DownloadableDocument skippedDocument);
    void onDownloadFailed(DownloadableDocument failedDocument, Exception cause);
    void onFinishedDownloading();
}
