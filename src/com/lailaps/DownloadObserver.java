package com.lailaps;

import com.lailaps.download.DownloadableDocument;

public interface DownloadObserver {
    void addDownload(DownloadableDocument document);
}
