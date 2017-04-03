package com.lailaps.download;

public interface ObservableDownloadSource {
    void addObserver(DownloadObserver observer);
    void notifyObserversStart(DownloadableDocument documentToDownload);
    void notifyObserversProgress(DownloadableDocument downloadableDocument, double progress);
    void notifyObserversSkipped(DownloadableDocument skippedDocument);
    void notifyObserversFailed(DownloadableDocument failedDocument, Exception cause);
    void notifyObserversSuccess(DownloadableDocument downloadedDocument);
    void notifyObserversEnd(DownloadStatistics statistics);
}
