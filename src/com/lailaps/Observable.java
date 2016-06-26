package com.lailaps;


import com.lailaps.download.DownloadableDocument;

public interface Observable {
    void addObserver(DownloadObserver observer);
    void removeObserver(DownloadObserver observer);
    void notifyObservers(DownloadableDocument downloadedDocument);
}
