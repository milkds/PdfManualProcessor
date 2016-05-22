package PdfManualProcessor.service;

import PdfManualProcessor.Manual;

import java.awt.*;
import java.net.URI;

public class ManualPageOpener {
    private Manual manual;
    private static final String URL_START = "http://74.117.180.69:83/work/pdfapprove/index.php?action=pdf&id=";

    public ManualPageOpener(Manual manual) {
        this.manual = manual;
    }

    public void open() throws Exception {
            Desktop desktop = Desktop.getDesktop();
            URI url = new URI(URL_START+manual.getId());
            desktop.browse(url);

        //implement writing to processedManuals.
    }
}
