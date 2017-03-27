package com.lailaps.download;


public class DownloadStatistics {
    private int downloadCount, skippedCount, failedCount;

    public void incrementDownloadCount() {
        downloadCount++;
    }

    public void incrementSkippedCount() {
        skippedCount++;
    }

    public void incrementFailedCount() {
        failedCount++;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }
}
