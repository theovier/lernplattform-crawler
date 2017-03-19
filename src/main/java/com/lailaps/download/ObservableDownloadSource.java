package com.lailaps.download;

public interface ObservableDownloadSource {
    void addObserver(DownloadObserver observer);
    void notifyObserversStart(DownloadableDocument downloadedDocument);
    void notifyObserversSkipped(DownloadableDocument skippedDocument, boolean isError);
    void notifyObserversEnd();
    void notifyObserversProgress(DownloadableDocument downloadableDocument, double progress);
}
