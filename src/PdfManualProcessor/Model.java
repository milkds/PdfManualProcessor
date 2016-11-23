package PdfManualProcessor;

import PdfManualProcessor.multithreading.MultithreadingController;
import PdfManualProcessor.service.ManualPageOpener;
import PdfManualProcessor.service.ManualSerializer;
import PdfManualProcessor.view.LongActionProgressBar;

import java.io.IOException;
import java.util.List;

/**
 * This class implements logic of main program functions.
 */
public class Model {
    private Controller controller;
    private CurrentAction currentAction;

    private MultithreadingController multithreadingController;

    public void setCurrentAction(CurrentAction currentAction) {
        this.currentAction = currentAction;
    }

    /**
     * Returns manual with status WAIT by default.
     * @param controller Controller object.
     */
    public Model(Controller controller) {
        this.controller = controller;
        currentAction=CurrentAction.WAIT;
    }
    /**
    Calls Manual list refresh.
     */
    public void refreshManualList() {
        try {
            MultithreadingController.refreshManualList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls Manual's opening in browser.
     * @param manualsToOpenQuantity - Quantity of manuals to open defined by User.
     */
    public void openNextManualsInBrowser(int manualsToOpenQuantity){
        //Getting list of manuals to open.
        List<Manual> manualsToOpen = ManualSerializer.getManualsForOpening();

        //Getting total quantity of manuals to open
        int size = manualsToOpen.size();

        //Checking that User haven't chosen to open more manuals that we have available.
        if (manualsToOpenQuantity>size)manualsToOpenQuantity=size-1;

        //Opening Manuals in browser.
        for (int i = 0; i <= manualsToOpenQuantity ; i++) {
            Manual manual = manualsToOpen.get(i);
            try {
             ManualPageOpener.open(manual,true);
            } catch (Exception e) {
                System.out.println("cannot open manual - " + manual.getPdfUrl());
            }
        }
    }

    /**
     * Calls previously opened Manual's opening in browser.
     * @param manualsToOpenQuantity - Quantity of manuals to open defined by User.
     */
    public void openPrevManualsInBrowser(int manualsToOpenQuantity) {
        //Getting list of manuals to open.
        List<Manual> manualsToOpen = ManualSerializer.getManualsForReopening();

        //Getting total quantity of manuals to open
        int size = manualsToOpen.size();

        //Checking that User haven't chosen to open more manuals that we have available.
        if (manualsToOpenQuantity>size)manualsToOpenQuantity=size-1;

        //Opening Manuals in browser.
        for (int i = 0; i <= manualsToOpenQuantity ; i++) {
            Manual manual = manualsToOpen.get(i);
            try {
                ManualPageOpener.open(manual,false);
            } catch (Exception e) {
                System.out.println("cannot open manual - " + manual.getPdfUrl());
            }
        }
    }

    /**
     * Deletes manuals in system.
     * @param manuals - List of manuals to delete.
     */
    public static void deleteManualsInConsole(List<Manual> manuals){
        try {
            MultithreadingController.deleteManualsInConsole(manuals);
        } catch (IOException ignored) {
        }
    }

    /**
     * Cancels long action.
     */
    public void cancelLongAction() {
        //User notification.
        System.out.println("Cancelling");

        //Checking which action is currently going and calling its cancellation.
        switch (currentAction){
            case CHECK:{
                multithreadingController.cancelManualFiltration();
                break;
            }
            case DOWNLOAD:{
                multithreadingController.cancelDownloadManuals();
                break;
            }
        }

        //User notification.
        System.out.println("Cancelled.");
    }

    /**
     * Downloads manuals.
     * @param progressBar - Progress bar for view during this action.
     */
    public void downloadManuals(LongActionProgressBar progressBar) {
        //Setting current action.
        setCurrentAction(CurrentAction.DOWNLOAD);

        //Downloading manuals.
        multithreadingController = new MultithreadingController();
        multithreadingController.downloadManuals(progressBar);
    }

    /**
     * Filters manuals.
     * @param progressBar - Progress bar for view during this action.
     */
    public void filterManuals(LongActionProgressBar progressBar) {
        //Setting current action.
        setCurrentAction(CurrentAction.CHECK);

        //Filtering manuals.
        multithreadingController = new MultithreadingController();
        multithreadingController.filterManuals(progressBar);
    }
}

    //todo: Remove controller variable, as it never used.