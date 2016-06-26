package com.lailaps.download;


public interface Observable {
    void addObserver(DownloadObserver observer);
    void removeObserver(DownloadObserver observer);
    void notifyObservers(DownloadableDocument downloadedDocument);
}
