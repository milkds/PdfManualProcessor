package PdfManualProcessor.view.strategy;

public interface Strategy {

    String[] getManualList();
    void onRemove(String[] manualsIds);
    void onRemoveAll();
}
