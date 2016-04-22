package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * This class downloads Manuals to Disk.
 *
 * Main idea of class is next:
 *
 * Ask for file size (add new field to manual class)
 * try to download Manual from proxy.
 * check downloaded file size. If it is not equal to file size - try to get it by direct link.
 *
 * If file size is not reachable - check file consistency via PDFReader.
 *
 * we have 3 types of urls - ftp, http and https
 *
 * http and https we do the same - parse host from urlString. Send head request to it - get Content-Length field. //this is not actual - use httpClient
 *
 * For ftp we use algorithm from sampleFTPMethod
 *
 *
 *
 */
public class ManualDownloader implements Runnable {
    private BlockingQueue<Manual> downloadingQueue;
    private final BlockingQueue<List<Manual>> manualWritingQueue;

    public ManualDownloader(BlockingQueue<Manual> downloadingQueue, BlockingQueue<List<Manual>> manualWritingQueue) {
        this.downloadingQueue = downloadingQueue;
        this.manualWritingQueue = manualWritingQueue;
    }
    //

    public static void downloadManual(Manual m){
        System.out.println("Manual downloaded: "+m.getPdfUrl());
    }


    /*public String call() throws Exception {
        while (true){
            Manual m = downloadingQueue.take();
            if (m.equals(ManualProducingController.TOXIC_MANUAL)){
                downloadingQueue.put(m);
                break;
            }
            downloadManual(m);
            manualWritingQueue.put(Collections.singletonList(m));
        }
        return "";
    }*/

    @Override
    public void run() {
        while (true){
            Manual m = null;
            try {
                m = downloadingQueue.take();
                if (m==null){
                    downloadingQueue.put(m);
                    break;
                }
                downloadManual(m);
                manualWritingQueue.put(Collections.singletonList(m));
            } catch (InterruptedException e) {
              break;
            }
        }

    }
}
