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

    public static void main(String[] args) throws InterruptedException {
        try {
            CookieStore cookieStore = LoginHandler.getCookies("login","password");
            getManuals(50,70,cookieStore);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     *
     * Need to put Manuals to downloadQueue to make second writer write them.
     */

    public static void getManuals(int startPage, int finishPage, CookieStore cookieStore) throws InterruptedException {
        BlockingQueue<String> htmlPageQueue = new LinkedBlockingQueue();
        BlockingQueue<List<Manual>> manualWritingQueue = new LinkedBlockingQueue();
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
            Future<String>ft = service.submit(new HtmlPageProcessor(htmlPageQueue,manualWritingQueue) {
            });
            processorFutures.add(ft);
        }
        for (int i = 0; i <10 ; i++) {
            Future<String>ft = service.submit(new ManualDownloader(downloadingQueue) {
            });
            downloadingFutures.add(ft);
        }
        new Thread(new ManualToFileWriter(manualWritingQueue)).start();
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
                System.out.println("processors running");
                TimeUnit.SECONDS.sleep(1);
        }
        manualWritingQueue.put(TOXIC_LIST);
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
