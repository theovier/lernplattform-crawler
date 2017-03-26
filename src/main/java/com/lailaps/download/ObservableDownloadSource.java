package com.lailaps.download;

public interface ObservableDownloadSource {
    void addObserver(DownloadObserver observer);
    void notifyObserversStart(DownloadableDocument downloadedDocument);
    void notifyObserversProgress(DownloadableDocument downloadableDocument, double progress);
    void notifyObserversSkipped(DownloadableDocument skippedDocument);
    void notifyObserversFailed(DownloadableDocument failedDocument, Exception cause);
    void notifyObserversEnd(int downloaded);
}
