package com.lailaps.download;


public class DownloadStatistics {
    private int downloadCount, skippedCount, failedCount;
    private String downloadFolderLocation;

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

    public void setDownloadFolderLocation(String location) {
        downloadFolderLocation = location;
    }

    public String getDownloadFolderLocation() {
        return downloadFolderLocation;
    }
}
