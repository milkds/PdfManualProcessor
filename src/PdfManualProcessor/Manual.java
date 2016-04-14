package PdfManualProcessor;

import PdfManualProcessor.service.ManualReader;

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

    public String getBody(int numberOfPages){
        return ManualReader.readStartingPages(this,numberOfPages);
    }


    // TODO:  realize getBody() method.
}
