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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadableDocument that = (DownloadableDocument) o;

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
        return result;
    }
}
