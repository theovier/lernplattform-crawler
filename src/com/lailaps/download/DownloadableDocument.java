package com.lailaps.download;

public class DownloadableDocument {

    private String name, downloadLink, courseName, fileExtension;

    public DownloadableDocument(String name, String downloadLink, String courseName, String fileExtension) {
        this.name = name;
        this.downloadLink = downloadLink;
        this.courseName = courseName;
        this.fileExtension = fileExtension;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getName() {
        return name;
    }

    public String getFolderName() {
        return courseName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public String toString() {
        return courseName + ": " + name + fileExtension;
    }

    public String toShortString(int maxLength) {
        String displayName = courseName + ": " + name;
        if (displayName.length() > maxLength) {
            displayName = displayName.substring(0, maxLength);
            displayName += "[...]";
        }
        displayName += fileExtension;
        return displayName;
    }
}
