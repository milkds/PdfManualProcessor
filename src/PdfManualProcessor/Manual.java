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
    public Manual(String id, String pdfUrl,int size) {
        this.id=id;
        this.pdfUrl = pdfUrl;
        this.size=size;
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

    public int getSize() {
        return size;
    }


    // TODO:  realize getBody() method.
}
