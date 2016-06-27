package com.lailaps.download;

public interface Observable {
    void addObserver(DownloadObserver observer);
    void notifyObserversSuccess(DownloadableDocument downloadedDocument);
    void notifyObserversSkipped(DownloadableDocument skippedDocument);
    void notifyObserversEnd();
}
