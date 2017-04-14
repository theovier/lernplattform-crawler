package com.lailaps.download;


import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class DownloadStatistics {
    private final ResourceBundle BUNDLE = ResourceBundle.getBundle("languages.UIResources");
    private final String TIME_FORMAT = "%02d:%02d min";
    private final String DESCRIPTION_TIME = BUNDLE.getString("download.statistics.time");
    private final String DESCRIPTION_DOWNLOAD_COUNT = BUNDLE.getString("download.statistics.successCounter");
    private final String DESCRIPTION_SKIPPED_COUNT = BUNDLE.getString("download.statistics.skippedCounter");
    private final String DESCRIPTION_FAILED_COUNT = BUNDLE.getString("download.statistics.failedCounter");

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

    public List<Pair> getDisplayableStats() {
        List<Pair> stats = new ArrayList<>();
        Pair<String, String> stat_time = new Pair<>(DESCRIPTION_TIME, getFormattedElapsedTime());
        Pair<String, Integer> stat_download = new Pair<>(DESCRIPTION_DOWNLOAD_COUNT, getDownloadCount());
        Pair<String, Integer> stat_skipped = new Pair<>(DESCRIPTION_SKIPPED_COUNT, getSkippedCount());
        Pair<String, Integer> stat_failed = new Pair<>(DESCRIPTION_FAILED_COUNT, getFailedCount());
        stats.add(stat_time);
        stats.add(stat_download);
        stats.add(stat_skipped);
        stats.add(stat_failed);
        return stats;
    }
}
