
public class PDFDocument {

    private String name, downloadLink;

    public PDFDocument(String name, String downloadLink) {
        this.name = name;
        this.downloadLink = downloadLink;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getName() {
        return name;
    }
}
