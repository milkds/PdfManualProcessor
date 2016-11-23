package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.awt.*;
import java.net.URI;
import java.util.Collections;

/**
 * This class implements logic of manual opening in browser.
 */
public class ManualPageOpener {

    private static final String URL_START = "http://74.117.180.69:83/work/pdfapprove/index.php?action=pdf&id=";

    /**
     * Opens manual in browser. Also saves manual as opened if necessary.
     * @param manual - manual to open.
     * @param saveToFile - Save to file as opened if true.
     * @throws Exception
     */
    public static void open(Manual manual, boolean saveToFile) throws Exception {
        openManualPage(manual);
        if (saveToFile) saveManualAsOpened(manual);
    }

    /**
     * Saves manual as opened.
     * @param manual - Manual to save.
     */
    private static void saveManualAsOpened(Manual manual){
        ManualSerializer.saveOpenedManualsToFile(Collections.singletonList(manual));
    }

    /**
     * Opens manual in browser.
     * @param manual - manual to open in browser.
     * @throws Exception
     */
    private static void openManualPage(Manual manual)throws Exception{
        Desktop desktop = Desktop.getDesktop();
        URI url = new URI(URL_START+manual.getId());
        desktop.browse(url);
    }
}
