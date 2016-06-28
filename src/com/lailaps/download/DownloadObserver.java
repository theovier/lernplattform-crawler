package com.lailaps.download;


public interface DownloadObserver {
    void addDownload(DownloadableDocument document);
    void skippedDownload(DownloadableDocument skippedDocument, boolean isError);
    void finishedDownloading();
}
