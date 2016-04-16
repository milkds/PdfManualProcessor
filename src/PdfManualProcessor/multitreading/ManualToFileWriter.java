package PdfManualProcessor.multitreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ManualToFileWriter implements Runnable {
    private final BlockingQueue<List<Manual>> manualWritingQueue;

    public ManualToFileWriter(BlockingQueue<List<Manual>> manualWritingQueue) {
        this.manualWritingQueue = manualWritingQueue;
    }

    @Override
    public void run() {
        while (true){
            List<Manual> manuals = null;
            try{
                manuals = manualWritingQueue.take();
                System.out.println(manuals.size());
                if (manuals.equals(ManualProducingController.TOXIC_LIST)){
                    System.out.println("got toxic list");
                    Thread.currentThread().interrupt();
                }
                ManualSerializer.saveRawManualsToFile(manuals);
                System.out.println("manuals saved");
             }
           catch (InterruptedException e){
               break;
           }

        }
    }
}
