package PdfManualProcessor.view;

import PdfManualProcessor.Controller;

public class ViewHandler {
    private Controller controller;
    public void fireEventRefreshManualList() {
        controller.onRefreshManualList();
    }
}
