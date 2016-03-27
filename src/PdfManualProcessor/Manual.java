package PdfManualProcessor;

public class Manual {

    private String pdfUrl;
    private String id;


    public Manual(String id, String pdfUrl) {
        this.pdfUrl = pdfUrl;
        this.id=id;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getId() {
        return id;
    }

    // TODO:  initialize id
}
