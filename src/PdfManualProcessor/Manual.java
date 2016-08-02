package PdfManualProcessor;

/**
 * This is Manual object class. Manual's ID is always unique and its used
 * here for equals and compareTo methods.
 */
public class Manual implements Comparable {

    private String pdfUrl; //Manual's URL.
    private String id; //Manuals ID in System.
    private int size; //Manuals estimated size.

    /**
     * Returns Manual with 0 size - when no size is available.
     * @param id - Manual's ID.
     * @param pdfUrl- Manual's URL.
     */
    public Manual(String id, String pdfUrl) {
        this.id = id;
        this.pdfUrl = pdfUrl;
        this.size = 0;
    }

    /**
     * Returns Manual with existing size - when no size is available.
     * @param id - Manual's ID.
     * @param pdfUrl- Manual's URL.
     */
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

    public void setSize(int size) {
        this.size = size;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
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
