package com.lailaps;

public class DownloadableDocument {

    //todo change to com.lailaps.DownloadableDocument -> auch .doc
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
        return name + ", " + courseName + ", " + downloadLink;
    }
}
