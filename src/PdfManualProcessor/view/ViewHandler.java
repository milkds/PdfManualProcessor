package PdfManualProcessor.view;

import PdfManualProcessor.Controller;

public class ViewHandler {
    private Controller controller;

    public ViewHandler(Controller controller) {
        this.controller = controller;
    }

    public void fireEventRefreshManualList() {
        controller.onRefreshManualList();
    }

    public void fireEventOpenNextManualsInBrowser(int value) {
        controller.onOpenNextManualsInBrowser(value);
    }

    public void fireEventOpenPrevManualsInBrowser(int i) {
        controller.onOpenPrevManualsInBrowser(i);
    }
}
