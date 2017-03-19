package com.lailaps.download;

public interface DownloadObserver {
    void startDownload(DownloadableDocument document);
    void skippedDownload(DownloadableDocument skippedDocument, boolean isError);
    void finishedDownloading();
    void onDownloadProgress(DownloadableDocument document, double currentProgress);
}
