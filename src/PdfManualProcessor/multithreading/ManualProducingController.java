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
    static final String TOXIC_WORD = "toxic";
    static final List<Manual> TOXIC_LIST = new ArrayList<>();
    static final Manual TOXIC_MANUAL = new Manual("","",0);

    private static final BlockingQueue<String> htmlPageQueue;

    public static void main(String[] args) throws InterruptedException {
        try {
            CookieStore cookieStore = LoginHandler.getCookies("login","password");
            getManuals(80,90,cookieStore);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ManualProducingController() {
    }

    public static void getManuals(int startPage, int finishPage, CookieStore cookieStore) throws InterruptedException {
        BlockingQueue<String> htmlPageQueue = new LinkedBlockingQueue();
        BlockingQueue<List<Manual>> manualWritingQueue = new LinkedBlockingQueue();
        BlockingQueue<List<Manual>> downloadedManualWritingQueue = new LinkedBlockingQueue();
        BlockingQueue<Manual> downloadingQueue = initDownloadingQueue();
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
        new Thread(new ManualToFileWriter(manualWritingQueue, ManualSerializer.getRawDataFile())).start();
        new Thread(new ManualToFileWriter(downloadedManualWritingQueue, ManualSerializer.getDownloadedManualFile())).start();
     //   new Thread(new ManualToFileWriter(manualWritingQueue)).start();
        while (isRunning(producerFutures)){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
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
}
