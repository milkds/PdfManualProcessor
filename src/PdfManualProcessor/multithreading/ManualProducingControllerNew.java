package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ManualProducingControllerNew {

    public static void main(String[] args) throws InterruptedException {
        downloadManuals(new ArrayList<Manual>(),10);
    }

    public static void downloadManuals(List<Manual> rawManuals, int numberOfTreads) throws InterruptedException {
        BlockingQueue<Manual> downloadingQueue = new LinkedBlockingQueue<>();
        BlockingQueue<List<Manual>> writingQueue = new LinkedBlockingQueue<>();
        for (Manual m: rawManuals){
            downloadingQueue.put(m);
        }
        for (int i = 0; i < numberOfTreads ; i++) {
            new Thread(new ManualDownloader(downloadingQueue,writingQueue)).start();
        }
        new Thread(new ManualToFileWriter(writingQueue, ManualSerializer.getDownloadedManualFile())).start();
    }

    public static void refreshManualList(){

    }
}
