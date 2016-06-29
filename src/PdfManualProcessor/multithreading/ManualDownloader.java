package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualDownloadUtil;
import PdfManualProcessor.service.ManualSerializer;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is a task in constant loop, which takes manual from queue and downloads it.
 */
public class ManualDownloader implements Runnable {

    private BlockingQueue<Manual> downloadingQueue;
    private static final String NOTIFY_FORMAT = "downloaded %d manuals of total %d in %d ms. %s - %s";
    private final AtomicInteger counter;
    private  Integer total;

    public ManualDownloader(BlockingQueue<Manual> downloadingQueue, AtomicInteger counter, Integer total) {
        this.downloadingQueue = downloadingQueue;
        this.counter = counter;
        this.total = total;
    }

    @Override
    public void run() {
        while (true) {
            Manual m = null;
            //getting start time (this needed for user notification).
            Long start = System.currentTimeMillis();
            try {
                //getting manual from queue.
                m = downloadingQueue.take();
                //if queue is empty, cancels whole task, as by design empty queue means that there is no manuals left to download
                if (m == null) {
                    downloadingQueue.put(m);
                    break;
                }
                //downloading manual.
                ManualDownloadUtil.download(m);
                //saving manual to list of downloaded manuals.
                serializeDownloadedManual(m);
                //notifying user.
                notifyUser(m,start,System.currentTimeMillis());
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * notifying user with quantity of manuals left to download and time taken for downloading current manual
     * @param m - current manual.
     * @param start - time of current manual downloading start.
     * @param finish - time of current manual downloading finish.
     */
    private void notifyUser(Manual m, Long start, Long finish){
        synchronized(counter) {
            System.out.println(String.format(NOTIFY_FORMAT, counter.incrementAndGet(), total, (finish - start), m.getPdfUrl(), m.getId()));
        }
    }
    /**
     * saving manuals to file with list of downloaded manuals.
     * @param m - downloaded manual to serialize.
     */
    private void serializeDownloadedManual(Manual m){
        ManualSerializer.saveDownloadedManualsToFile(Collections.singletonList(m));
    }

}
