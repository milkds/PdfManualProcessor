package PdfManualProcessor;

public class Manual {

    private String pdfUrl;
    private String id;

    public Manual(String id, String pdfUrl) {
        this.id=id;
        this.pdfUrl = pdfUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getId() {
        return id;
    }

    public String getBody(){

        return "";
    }

    @Override
    public String toString() {
        return "Manual{" +
                "pdfUrl='" + pdfUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
    // TODO:  realize getBody() method.
}
