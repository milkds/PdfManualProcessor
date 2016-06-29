package PdfManualProcessor.multithreading;

import PdfManualProcessor.service.ManualSizeChecker;

import java.util.concurrent.Callable;

/**
 * This class is a task, which returns manual estimated size or 0,
 * if exception is occurred during this task work.
 */
public class ManualSizeGetter implements Callable<Integer> {
    private final String manualUrl;

    public ManualSizeGetter(String manualUrl) {
        this.manualUrl = manualUrl;
    }

    @Override
    public Integer call() throws Exception {
        return ManualSizeChecker.getManualSize(manualUrl);
    }
}
