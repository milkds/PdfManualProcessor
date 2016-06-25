package PdfManualProcessor.view;

import PdfManualProcessor.Controller;

public class ViewHandler {
    private Controller controller;

    private Console console;

    private MainView mainView;
    private LongActionView longActionView;
    private ManualCheckView manualCheckView;

    public ViewHandler(Controller controller) {
        this.controller = controller;
        console = new Console();
        mainView = new MainView(console,this);
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

    public void fireEventCancelLongAction() {
        longActionView.setVisible(false);
        mainView = new MainView(console,this);
        controller.onCancelLongAction();
    }

    public void fireEventDownloadManuals() {
        mainView.setVisible(false);
        longActionView=new LongActionView(console,this);
        controller.onDownloadManuals(longActionView.getProgressBar());
    }

    public void fireEventFilterManuals() {
        mainView.setVisible(false);
        longActionView=new LongActionView(console,this);
        controller.onFilterManuals(longActionView.getProgressBar());
    }

    public void fireEventCheckManuals() {
        mainView.setVisible(false);
        manualCheckView = new ManualCheckView();
    }
}
