package PdfManualProcessor;

import PdfManualProcessor.service.ManualReader;
import PdfManualProcessor.service.ManualSizeChecker;

public class Manual implements Comparable {

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

    @Override
    public int compareTo(Object o) {
        Manual m = (Manual)o;
        return this.getId().compareTo(m.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manual)) return false;
        Manual manual = (Manual) o;

        return getPdfUrl().equals(manual.getPdfUrl()) && getId().equals(manual.getId());
    }

    @Override
    public int hashCode() {
        int result = getPdfUrl().hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
