package com.lailaps.download;

import com.lailaps.crawler.Term;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class DownloadableDocument {

    private String name, downloadLink, courseName, fileExtension, resourceID;
    private long size = 0L;
    private final Term correspondingTerm;

    public DownloadableDocument(String name, String downloadLink, String courseName, String fileExtension, Term term) {
        this.name = name;
        this.downloadLink = downloadLink;
        this.courseName = courseName;
        this.fileExtension = fileExtension;
        this.correspondingTerm = term;
        this.resourceID = extractResourceID(downloadLink);
    }

    public DownloadableDocument(String name, String downloadLink, String courseName, String fileExtension, long size, Term term) {
        this.name = name;
        this.downloadLink = downloadLink;
        this.courseName = courseName;
        this.fileExtension = fileExtension;
        this.size = size;
        this.correspondingTerm = term;
        this.resourceID = extractResourceID(downloadLink);
    }

    //rename constructor (adds its resourceID to the name)
    private DownloadableDocument(DownloadableDocument copy) {
        this.name = copy.name + String.format(" (%s)", copy.resourceID);
        this.downloadLink = copy.downloadLink;
        this.courseName = copy.courseName;
        this.fileExtension = copy.fileExtension;
        this.size = copy.size;
        this.correspondingTerm = copy.correspondingTerm;
        this.resourceID = copy.resourceID;
    }

    public static DownloadableDocument getRenamedDocument(DownloadableDocument copy) {
        return new DownloadableDocument(copy);
    }

    private String extractResourceID(String downloadLink) {
        return StringUtils.substringBetween(downloadLink, ".php/", "/");
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

    public String getResourceID() {
        return this.resourceID;
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
        return correspondingTerm.getDirectoryFriendlyName() + File.separator + getFolderName() + File.separator + getName() + getFileExtension();
    }
}
