package com.lailaps.download;

//todo rename
public interface Observable {
    void addObserver(DownloadObserver observer);
    void notifyObserversSuccess(DownloadableDocument downloadedDocument);
    void notifyObserversSkipped(DownloadableDocument skippedDocument, boolean isError);
    void notifyObserversEnd();
}
