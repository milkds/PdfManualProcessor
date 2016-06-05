package PdfManualProcessor;

import PdfManualProcessor.multithreading.ManualProducingControllerNew;
import PdfManualProcessor.service.ManualPageOpener;
import PdfManualProcessor.service.ManualSerializer;

import java.io.IOException;
import java.util.List;

public class Model {
    private Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
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
}
