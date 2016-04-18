package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;

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
public class ManualDownloader implements Callable<String> {
    private BlockingQueue<Manual> downloadingQueue;

    public ManualDownloader(BlockingQueue<Manual> downloadingQueue) {
        this.downloadingQueue = downloadingQueue;
    }
    //

    public static void donwloadManual(Manual m){
        System.out.println("Manual downloaded: "+m.getPdfUrl());
    }

    @Override
    public String call() throws Exception {
        return "";
    }
}
