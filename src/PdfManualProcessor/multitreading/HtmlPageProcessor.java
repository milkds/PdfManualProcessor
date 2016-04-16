package PdfManualProcessor.multitreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualPageParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class HtmlPageProcessor implements Runnable{

    private final BlockingQueue<String> htmlPageQueue;
    private final BlockingQueue<List<Manual>> manualWritingQueue;

    public HtmlPageProcessor(BlockingQueue<String> queue, BlockingQueue<List<Manual>> manualWritingQueue) {
        this.htmlPageQueue = queue;
        this.manualWritingQueue = manualWritingQueue;
    }

    @Override
    public void run() {
       while (true){
           try {
               String pageBody = htmlPageQueue.take();
               if(ManualProducingController.TOXIC_WORD.equals(pageBody)){
                   manualWritingQueue.put(new ArrayList<Manual>(ManualProducingController.TOXIC_SIZE));
                   Thread.currentThread().interrupt();
               }
               List<Manual> manuals = ManualPageParser.getManuals(pageBody);
               manualWritingQueue.put(manuals);
           }
           catch (InterruptedException ignored) {
               System.out.println(Thread.currentThread().getName()+" thread finished.");
           }
       }
    }
}
