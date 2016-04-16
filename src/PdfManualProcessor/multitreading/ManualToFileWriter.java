package PdfManualProcessor.multitreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ManualToFileWriter implements Runnable {
    private final BlockingQueue<List<Manual>> manualWritingQueue;

    public ManualToFileWriter(BlockingQueue<List<Manual>> manualWritingQueue) {
        this.manualWritingQueue = manualWritingQueue;
    }

    @Override
    public void run() {
        while (true){
            List<Manual> manuals = manualWritingQueue.poll();
            if (manuals.size()==ManualProducingController.TOXIC_SIZE)Thread.currentThread().interrupt();
            ManualSerializer.saveRawManualsToFile(manuals);
        }
    }
}
