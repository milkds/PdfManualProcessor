package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualDownloadUtil;
import PdfManualProcessor.service.ManualSerializer;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ManualDownloaderNew implements Runnable {

    private BlockingQueue<Manual> downloadingQueue;
    private static final String NOTIFY_FORMAT = "downloaded %d manuals of total %d in %d ms. %s - %s";
    private final AtomicInteger counter;
    private  Integer total;

    public ManualDownloaderNew(BlockingQueue<Manual> downloadingQueue, AtomicInteger counter, Integer total) {
        this.downloadingQueue = downloadingQueue;
        this.counter = counter;
        this.total = total;
    }

    @Override
    public void run() {
        while (true) {
            Manual m = null;
            Long start = System.currentTimeMillis();
            try {
                m = downloadingQueue.take();
                if (m == null) {
                    downloadingQueue.put(m);
                    break;
                }
                ManualDownloadUtil.download(m);
                serializeDownloadedManual(m);
                notifyUser(m,start,System.currentTimeMillis());
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void notifyUser(Manual m, Long start, Long finish){
        synchronized(counter) {
            System.out.println(String.format(NOTIFY_FORMAT, counter.incrementAndGet(), total, (finish - start), m.getPdfUrl(), m.getId()));
        }
    }

    private void serializeDownloadedManual(Manual m){
        ManualSerializer.saveDownloadedManualsToFile(Collections.singletonList(m));
    }

}
