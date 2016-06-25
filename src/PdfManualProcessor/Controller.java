package PdfManualProcessor;

import PdfManualProcessor.view.LongActionProgressBar;
import PdfManualProcessor.view.ViewHandler;

public class Controller {
    private Model model;

    public static void main(String[] args) throws InterruptedException {
        Controller controller = new Controller();
        Model model = new Model(controller);
        controller.setModel(model);
        ViewHandler viewHandler = new ViewHandler(controller);
    }

    public void onRefreshManualList() {
        model.refreshManualList();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void onOpenNextManualsInBrowser(int value) {
        model.openNextManualsInBrowser(value);
    }

    public void onOpenPrevManualsInBrowser(int i) {
        model.openPrevManualsInBrowser(i);
    }

    public void onCancelLongAction() {
        model.cancelLongAction();
    }

    public void onDownloadManuals(LongActionProgressBar progressBar) {
        model.downloadManuals(progressBar);
    }

    public void onFilterManuals(LongActionProgressBar progressBar) {
        model.filterManuals(progressBar);
    }
}
