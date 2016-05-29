package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ManualPageOpener {

    private static final String URL_START = "http://74.117.180.69:83/work/pdfapprove/index.php?action=pdf&id=";

    public static void open(Manual manual, boolean saveToFile) throws Exception {
        openManualPage(manual);
        if (saveToFile) saveManualAsOpened(manual);
    }

    private static void saveManualAsOpened(Manual manual){
        List<Manual> manuals = new ArrayList<>();
        manuals.add(manual);
        ManualSerializer.saveOpenedManualsToFile(manuals);
    }
    private static void openManualPage(Manual manual)throws Exception{
        Desktop desktop = Desktop.getDesktop();
        URI url = new URI(URL_START+manual.getId());
        desktop.browse(url);
    }

    //Check if manual already processed (this used for opening previous manuals)
    private boolean isManualProcessed(Manual manual){
        return false;
    }
}
