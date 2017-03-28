package com.lailaps.download;

import java.io.File;

public class DownloadableDocument {

    private String name, downloadLink, courseName, fileExtension;
    private long size = 0L;

    public DownloadableDocument(String name, String downloadLink, String courseName, String fileExtension) {
        this.name = name;
        this.downloadLink = downloadLink;
        this.courseName = courseName;
        this.fileExtension = fileExtension;
    }

    public DownloadableDocument(String name, String downloadLink, String courseName, String fileExtension, long size) {
        this.name = name;
        this.downloadLink = downloadLink;
        this.courseName = courseName;
        this.fileExtension = fileExtension;
        this.size = size;
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

    public long getSize() {
        return size;
    }

    public void setSize(long downloadSize) {
        this.size = downloadSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadableDocument that = (DownloadableDocument) o;

        if (size != that.size) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (downloadLink != null ? !downloadLink.equals(that.downloadLink) : that.downloadLink != null) return false;
        if (courseName != null ? !courseName.equals(that.courseName) : that.courseName != null) return false;
        return fileExtension != null ? fileExtension.equals(that.fileExtension) : that.fileExtension == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (downloadLink != null ? downloadLink.hashCode() : 0);
        result = 31 * result + (courseName != null ? courseName.hashCode() : 0);
        result = 31 * result + (fileExtension != null ? fileExtension.hashCode() : 0);
        result = 31 * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return courseName + ": " + name + fileExtension;
    }

    public String getSaveLocation() {
        return getFolderName() + File.separator + getName() + getFileExtension();
    }
}
