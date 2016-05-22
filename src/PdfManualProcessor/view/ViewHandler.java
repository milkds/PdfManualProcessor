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

    public void fireEventOpenManualsInBrowser(int value) {
        controller.onOpenManualsInBrowser(value);
    }
}
