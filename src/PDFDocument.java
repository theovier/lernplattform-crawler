
public class PDFDocument {

    //todo change to DownloadableDocument -> auch .doc
    private String name, downloadLink, courseName, term;

    public PDFDocument(String name, String downloadLink, String courseName) {
        this.name = name;
        this.downloadLink = downloadLink;
        this.courseName = courseName;
       // this.term = fetchTerm();
    }

    /*
    private String fetchTerm() {
        String abbreviatedTerm = courseName.split(" ")[0]; //SS2016, WS2015
        String termToken = abbreviatedTerm.substring(0,2);
        String year = abbreviatedTerm.substring(2);


        String
        switch(termToken) {
            case "SS":

        }

        return "Sommersemester 2016";
    }
    */

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getName() {
        return name;
    }

    public String getFolderName() {
        return courseName;
    }

    public String getTerm() {
        return term;
    }

    public String getFileExtension() {
        return ".pdf";
    }

    @Override
    public String toString() {
        return name + ", " + courseName + ", " + downloadLink;
    }
}
