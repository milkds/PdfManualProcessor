package PdfManualProcessor;

import PdfManualProcessor.view.Console;
import PdfManualProcessor.view.MainView;
import PdfManualProcessor.view.ViewHandler;

public class Controller {
    private Model model;

    public static void main(String[] args) throws InterruptedException {
        Controller controller = new Controller();
        Model model = new Model(controller);
        controller.setModel(model);
        ViewHandler viewHandler = new ViewHandler(controller);
        Console console = new Console();
        new MainView(console, viewHandler);
    }

    public void onRefreshManualList() {
        model.refreshManualList();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void onOpenManualsInBrowser(int value) {
        model.openManualsInBrowser(value);
    }
}
