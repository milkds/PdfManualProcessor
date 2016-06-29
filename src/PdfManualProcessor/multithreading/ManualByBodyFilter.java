package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualFilter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is a task in constant loop, which takes Manual and checks its body for containing words from dictionary.
 */
public class ManualByBodyFilter implements Runnable {

    private final AtomicInteger counter;
    private Integer total;
    private BlockingQueue<Manual> filteringQueue;
    private List<String> sureDeleteDictionary;
    private List<String> checkDeleteDictionary;

    private static final String NOTIFY_FORMAT = "checked %d manuals of total %d in %d ms. %s - %s";

    public ManualByBodyFilter(AtomicInteger counter, Integer total, BlockingQueue<Manual> filteringQueue, List<String> sureDeleteDictionary, List<String> checkDeleteDictionary) {
        this.counter = counter;
        this.total = total;
        this.filteringQueue = filteringQueue;
        this.sureDeleteDictionary = sureDeleteDictionary;
        this.checkDeleteDictionary = checkDeleteDictionary;
    }

    @Override
    public void run() {
        while (true){
            //getting time of start - this needed for user notification.
            Long start=System.currentTimeMillis();

            //getting manual from queue.
            Manual m = null;
            try {
                 m = filteringQueue.take();
                // if queue is empty - cancel whole task. As by design queue can be empty only when no tasks left.
                if (m==null){
                    filteringQueue.put(m);
                    break;
                }
            } catch (InterruptedException ignored) {
            }
            //finding whether manual body contains search words. Serialising results.
            ManualFilter.filterManualByBody(m,sureDeleteDictionary,checkDeleteDictionary);

            //notifying user
            notifyUser(m,start,System.currentTimeMillis());
        }
    }

    /**
     * notify user with quantity of manuals left to check and time taken to check current manual.
     * @param m - Manual, which has just been checked.
     * @param start - time of check start for current manual.
     * @param finish- time of check finish for current manual.
     */
    private void notifyUser(Manual m, Long start, Long finish){
        synchronized(counter) {
            System.out.println(String.format(NOTIFY_FORMAT, counter.incrementAndGet(), total, (finish - start), m.getPdfUrl(), m.getId()));
        }
    }
}
