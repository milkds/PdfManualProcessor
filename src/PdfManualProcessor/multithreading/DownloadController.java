package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadController {
    private BlockingQueue<Manual> downloadingQueue = new LinkedBlockingQueue<>();
    private  AtomicInteger counter;
    private Integer total;
    public void downloadManuals(){
        List<Manual> manuals = ManualSerializer.getManualsForDownload();
        total = manuals.size();
        counter = new AtomicInteger(0);

        for (Manual m : manuals){
            try {
                downloadingQueue.put(m);
                System.out.println(m.getId());
            } catch (InterruptedException ignored) {
            }
        }
        for (int i = 0; i <10; i++) {
            new Thread(new ManualDownloaderNew(downloadingQueue,counter, total)).start();
        }
    }

    public void cancelDownload(){
        downloadingQueue.clear();
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public Integer getTotal() {
        return total;
    }
}
