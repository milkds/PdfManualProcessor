package PdfManualProcessor.view;

import PdfManualProcessor.Controller;

/**
 * This class receives method calls from User and transfers them to Controller.
 * Also launches updates to View.
 */
public class ViewHandler {
    private Controller controller;

    private Console console;

    private MainView mainView;
    private LongActionView longActionView;
    private ManualCheckView manualCheckView;

    //Returns ViewHandler with launched MainView.
    public ViewHandler(Controller controller) {
        this.controller = controller;
        console = new Console();
        mainView = new MainView(console,this);
    }

    /**
     * Calls Manual List Refreshing.
     */
    public void fireEventRefreshManualList() {
        controller.onRefreshManualList();
    }

    /**
     * Calls Manual opening in Browser
     * @param ManualsToOpen - number of Manuals to open.
     */
    public void fireEventOpenNextManualsInBrowser(int ManualsToOpen) {
        controller.onOpenNextManualsInBrowser(ManualsToOpen);
    }

    /**
     * Calls opening in Browser of previously opened Manuals
     * @param ManualsToOpen - number of Manuals to open.
     */
    public void fireEventOpenPrevManualsInBrowser(int ManualsToOpen) {
        controller.onOpenPrevManualsInBrowser(ManualsToOpen);
    }

    /**
     * Calls cancellation of Long action currently being performed.
     */
    public void fireEventCancelLongAction() {
        //Hiding Long Action View.
        longActionView.setVisible(false);

        //Opening MainView.
        mainView = new MainView(console,this);

        //Calling for cancellation.
        controller.onCancelLongAction();
    }

    /**
     * Calls Manuals downloading start.
     */
    public void fireEventDownloadManuals() {
        //Hiding Main View.
        mainView.setVisible(false);

        //Opening new Long Action View.
        longActionView=new LongActionView(console,this);

        //Calling Manuals downloading start.
        controller.onDownloadManuals(longActionView.getProgressBar());
    }

    /**
     * Calls Manuals filtration start.
     */
    public void fireEventFilterManuals() {
        //Hiding Main View.
        mainView.setVisible(false);

        //Opening new Long Action View.
        longActionView=new LongActionView(console,this);

        //Calling Manuals filtration start.
        controller.onFilterManuals(longActionView.getProgressBar());
    }

    /**
     * Opens View for visual check of manuals bodies.
     */
    public void fireEventCheckManuals() {
        //Hiding Main View.
        mainView.setVisible(false);

        //Opening new Manual Check View
        manualCheckView = new ManualCheckView();
    }
}

//todo: Check method descriptions for possible improvements.