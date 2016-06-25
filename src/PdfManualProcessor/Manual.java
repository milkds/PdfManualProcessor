package PdfManualProcessor;

public class Manual implements Comparable {
    public void setSize(int size) {
        this.size = size;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    private String pdfUrl;
    private String id;
    private int size;

    public Manual(String id, String pdfUrl) {
        this.id = id;
        this.pdfUrl = pdfUrl;
        this.size = 0;
    }
    public Manual(String id, String pdfUrl,int size) {
        this.id = id;
        this.pdfUrl = pdfUrl;
        this.size = size;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getId() {
        return id;
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

        return getId().equals(manual.getId());
    }

    @Override
    public int hashCode() {
        int result = getPdfUrl().hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
