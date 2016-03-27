package PdfManualProcessor;

public class Manual {

    private String consoleUrl;
    private String pdfUrl;
    private String id;


    public Manual(String consoleUrl, String pdfUrl) {
        this.consoleUrl = consoleUrl;
        this.pdfUrl = pdfUrl;
        this.id=consoleUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getId() {
        return id;
    }

    // TODO:  initialize id
}
