package com.lailaps.download;


import java.util.concurrent.TimeUnit;

public class DownloadStatistics {
    private static final String TIME_FORMAT = "%02d:%02d min";
    private int downloadCount, skippedCount, failedCount;
    private String downloadFolderLocation, formattedElapsedTime;
    private long elapsedTime;

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

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
        this.formattedElapsedTime = setFormattedElapsedTime(elapsedTime);
    }

    private String setFormattedElapsedTime(long elapsedTime) {
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        long elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        long displayedElapsedSeconds = elapsedSeconds - TimeUnit.MINUTES.toSeconds(elapsedMinutes); //121 seconds - 120 seconds
        return String.format(TIME_FORMAT, elapsedMinutes, displayedElapsedSeconds);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getFormattedElapsedTime() {
        return formattedElapsedTime;
    }
}
