package PdfManualProcessor;

import PdfManualProcessor.service.ManualReader;
import PdfManualProcessor.service.ManualSizeChecker;

public class Manual {

    private String pdfUrl;
    private String id;
    private int size;

    public Manual(String id, String pdfUrl) {
        this.id=id;
        this.pdfUrl = pdfUrl;
        this.size= ManualSizeChecker.getManualSize(pdfUrl);
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
