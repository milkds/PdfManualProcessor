package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualPageParser;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class HtmlPageProcessor implements Callable{

    private final BlockingQueue<String> htmlPageQueue;
    private final BlockingQueue<List<Manual>> manualWritingQueue;


    public HtmlPageProcessor(BlockingQueue<String> queue, BlockingQueue<List<Manual>> manualWritingQueue) {
        this.htmlPageQueue = queue;
        this.manualWritingQueue = manualWritingQueue;
    }

    /*@Override
    public void run() {
       while (true){
           try {
               String pageBody = htmlPageQueue.take();
               if(pageBody.equals(ManualProducingController.TOXIC_WORD)){
                   manualWritingQueue.put(ManualProducingController.TOXIC_LIST); //to be reworked urgently.
                   htmlPageQueue.put(ManualProducingController.TOXIC_WORD);
                   Thread.currentThread().interrupt();
               }
               List<Manual> manuals = ManualPageParser.getManuals(pageBody);
               manualWritingQueue.put(manuals);
           }
           catch (InterruptedException ignored) {
               break;
           }
       }
    }*/

    @Override
    public Object call() throws Exception {
        System.out.println("processor started");
        while (true){
                String pageBody = htmlPageQueue.take();
            System.out.println("page taken");
                if(pageBody.equals(ManualProducingController.TOXIC_WORD)){
                    htmlPageQueue.put(ManualProducingController.TOXIC_WORD);
                    break;
                }
                List<Manual> manuals = ManualPageParser.getManuals(pageBody);
                manualWritingQueue.put(manuals);
        }
        return "";
    }
}
