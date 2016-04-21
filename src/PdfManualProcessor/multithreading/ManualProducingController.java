package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.LoginHandler;
import PdfManualProcessor.service.ManualSerializer;
import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ManualProducingController {
    /*static final String TOXIC_WORD = "toxic";
    static final List<Manual> TOXIC_LIST = new ArrayList<>();
    static final Manual TOXIC_MANUAL = new Manual("","",0);

    private final BlockingQueue<String> htmlPageQueue= new LinkedBlockingQueue();
    private final BlockingQueue<List<Manual>>  manualWritingQueue= new LinkedBlockingQueue();
    private final BlockingQueue<List<Manual>> downloadedManualWritingQueue= new LinkedBlockingQueue();
    private final BlockingQueue<Manual> downloadingQueue;

    public static void main(String[] args) throws InterruptedException {
        try {
            CookieStore cookieStore = LoginHandler.getCookies("login","password");
            new ManualProducingController().getManuals(80,90,cookieStore);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ManualProducingController() throws InterruptedException {
        downloadingQueue = initDownloadingQueue();
    }

    public void getManuals(int startPage, int finishPage, CookieStore cookieStore) throws InterruptedException {
     //  BlockingQueue<String> htmlPageQueue = new LinkedBlockingQueue();
       *//* BlockingQueue<List<Manual>> manualWritingQueue = new LinkedBlockingQueue();
        BlockingQueue<List<Manual>> downloadedManualWritingQueue = new LinkedBlockingQueue();
        BlockingQueue<Manual> downloadingQueue = initDownloadingQueue();*//*
        ExecutorService service = Executors.newCachedThreadPool();
        List<Future>producerFutures = new ArrayList<>();
        List<Future>processorFutures= new ArrayList<>();
        List<Future>downloadingFutures= new ArrayList<>();
        for (int i = startPage; i <finishPage ; i++) {
            Future<String>ft = service.submit(new HtmlPageProducer(htmlPageQueue,cookieStore,i) {
            });
            producerFutures.add(ft);
        }
        for (int i = 0; i <10 ; i++) {
            Future<String>ft = service.submit(new HtmlPageProcessor(htmlPageQueue,manualWritingQueue, downloadingQueue) {
            });
            processorFutures.add(ft);
        }
        for (int i = 0; i <10 ; i++) {
            Future<String>ft = service.submit(new ManualDownloader(downloadingQueue, manualWritingQueue) {
            });
            downloadingFutures.add(ft);
        }
       startWriters();
     //   new Thread(new ManualToFileWriter(manualWritingQueue)).start();
        while (isRunning(producerFutures)){
                TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("writer started");
        htmlPageQueue.put(TOXIC_WORD);
        while (isRunning(processorFutures)){
                TimeUnit.SECONDS.sleep(1);
        }
        manualWritingQueue.put(TOXIC_LIST);
        downloadingQueue.put(TOXIC_MANUAL);
        while (isRunning(downloadingFutures)){
            System.out.println("downloaders running");
            TimeUnit.SECONDS.sleep(15);
        }
        downloadedManualWritingQueue.put(TOXIC_LIST);
        service.shutdown();
    }


    public static boolean isRunning(List<Future> list){
        for (Future future : list){
            if (!future.isDone())return true;
        }
        return false;
    }
    public static BlockingQueue<Manual> initDownloadingQueue() throws InterruptedException {
        LinkedBlockingQueue result = new LinkedBlockingQueue();
        List<Manual> manuals = ManualSerializer.getManualsForDownload();
        for (Manual m : manuals){
            result.put(m);
        }

        return result;
    }
    public void startWriters(){
        new Thread(new ManualToFileWriter(manualWritingQueue, ManualSerializer.getRawDataFile())).start();
        new Thread(new ManualToFileWriter(downloadedManualWritingQueue, ManualSerializer.getDownloadedManualFile())).start();}*/


}
