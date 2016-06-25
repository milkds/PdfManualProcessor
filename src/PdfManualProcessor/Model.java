package PdfManualProcessor;

import PdfManualProcessor.multithreading.ManualProducingControllerNew;
import PdfManualProcessor.service.ManualPageOpener;
import PdfManualProcessor.service.ManualSerializer;
import PdfManualProcessor.view.LongActionProgressBar;

import java.io.IOException;
import java.util.List;

public class Model {
    private Controller controller;
    private CurrentAction currentAction;

    private ManualProducingControllerNew manualProducingControllerNew;

    public void setCurrentAction(CurrentAction currentAction) {
        this.currentAction = currentAction;
    }

    public Model(Controller controller) {
        this.controller = controller;
        currentAction=CurrentAction.WAIT;
    }

    public void refreshManualList() {
        try {
            ManualProducingControllerNew.refreshManualList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNextManualsInBrowser(int manualsToOpenQuantity){
        List<Manual> manualsToOpen = ManualSerializer.getManualsForOpening();
        int size = manualsToOpen.size();
        if (manualsToOpenQuantity>size)manualsToOpenQuantity=size-1;
        for (int i = 0; i <= manualsToOpenQuantity ; i++) {
            Manual manual = manualsToOpen.get(i);
            try {
             ManualPageOpener.open(manual,true);
            } catch (Exception e) {
                System.out.println("cannot open manual - " + manual.getPdfUrl());
            }
        }
    }

    public void openPrevManualsInBrowser(int manualsToOpenQuantity) {
        List<Manual> manualsToOpen = ManualSerializer.getManualsForReopening();
        int size = manualsToOpen.size();
        if (manualsToOpenQuantity>size)manualsToOpenQuantity=size-1;
        for (int i = 0; i <= manualsToOpenQuantity ; i++) {
            Manual manual = manualsToOpen.get(i);
            try {
                ManualPageOpener.open(manual,false);
            } catch (Exception e) {
                System.out.println("cannot open manual - " + manual.getPdfUrl());
            }
        }
    }

    public static void deleteManualsInConsole(List<Manual> manuals){
        ManualProducingControllerNew.deleteManualsInConsole(manuals);
    }

    public void cancelLongAction() {
        System.out.println("cancelled");
        switch (currentAction){
            case CHECK:{
                manualProducingControllerNew.cancelManualFiltration();
                break;
            }
            case DOWNLOAD:{
                manualProducingControllerNew.cancelDownloadManuals();
                break;
            }
        }
    }

    public void downloadManuals(LongActionProgressBar progressBar) {
        setCurrentAction(CurrentAction.DOWNLOAD);
        manualProducingControllerNew = new ManualProducingControllerNew();
        manualProducingControllerNew.downloadManuals(progressBar);
    }

    public void filterManuals(LongActionProgressBar progressBar) {
        setCurrentAction(CurrentAction.CHECK);
        manualProducingControllerNew = new ManualProducingControllerNew();
        manualProducingControllerNew.filterManuals(progressBar);
    }
}
