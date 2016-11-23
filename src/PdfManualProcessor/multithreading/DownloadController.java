package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class implements manual downloading on multithreading level.
 */

public class DownloadController {
    private BlockingQueue<Manual> downloadingQueue = new LinkedBlockingQueue<>();
    private AtomicInteger counter;
    private Integer total;

    /**
     * Starts threads which take manuals from downloadingQueue and download them.
     */
    public void downloadManuals(){
        //getting list of manuals to download.
        List<Manual> manuals = ManualSerializer.getManualsForDownload();

        //initialising total count of manuals to download (needed for user notification).
        total = manuals.size();

        //initialising counter of downloaded manuals. (needed for user notification).
        counter = new AtomicInteger(0);

        //filling downloadingQueue with manuals to download.
        for (Manual m : manuals){
            try {
                downloadingQueue.put(m);
            } catch (InterruptedException ignored) {
            }
        }

        //starting threads for downloading manuals. 10 seems to be optimal number, as download performed from different servers.
        for (int i = 0; i <10; i++) {
            new Thread(new ManualDownloader(downloadingQueue,counter, total)).start();
        }
    }
    /**
     * Cancels download by clearing queue (some time needed for each
     * thread to complete its current task - than it is shut down by itself after getting null manual from queue).
     */
    public void cancelDownload(){
        downloadingQueue.clear();
    }
    /**
     * @return Counter of downloaded manuals (for user notification).
     */
    public AtomicInteger getCounter() {
        return counter;
    }
    /**
     * @return Total count of manuals to download (for user notification).
     */
    public Integer getTotal() {
        return total;
    }
}
