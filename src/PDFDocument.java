
public class PDFDocument {

    private String name, downloadLink, courseName;

    public PDFDocument(String name, String downloadLink, String courseName) {
        this.name = name;
        this.downloadLink = downloadLink;
        this.courseName = courseName;
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

    @Override
    public String toString() {
        return name + ", " + downloadLink;
    }
}
