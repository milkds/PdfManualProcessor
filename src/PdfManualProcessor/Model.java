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

    public void openManualsInBrowser(int value){
        List<Manual> manualsToOpen = ManualSerializer.getManualsForOpening();
        int size = manualsToOpen.size();
        if (value>size)value=size;
        for (int i = 1; i <= value ; i++) {
            Manual manual = manualsToOpen.get(i-1);
            try {
                new ManualPageOpener(manual).open();
            } catch (Exception e) {
                System.out.println("cannot open manual - "+manual.getPdfUrl());
            }
        }
    }
}
