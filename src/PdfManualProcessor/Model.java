package PdfManualProcessor;

import PdfManualProcessor.multithreading.ManualProducingControllerNew;

import java.io.IOException;

public class Model {
    private Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
    }

    public void refreshManualList() {
        try {
            ManualProducingControllerNew.refreshManualList(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
