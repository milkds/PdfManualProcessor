package PdfManualProcessor.multitreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.LoginHandler;
import org.apache.http.client.CookieStore;
import sun.misc.ThreadGroupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ManualProducingController {
    static final String TOXIC_WORD = "toxic";
    static final List<Manual> TOXIC_LIST = new ArrayList<>();

    public static void main(String[] args) {
        try {
            CookieStore cookieStore = LoginHandler.getCookies("login","password");
            getManuals(50,70,cookieStore);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void getManuals(int startPage, int finishPage, CookieStore cookieStore){
        BlockingQueue<String> htmlPageQueue = new LinkedBlockingQueue();
        BlockingQueue<List<Manual>> manualWritingQueue = new LinkedBlockingQueue();
        ExecutorService service = Executors.newCachedThreadPool();
        List<Future>producerFutures = new ArrayList<>();
        List<Future>processorFutures= new ArrayList<>();
        for (int i = startPage; i <finishPage ; i++) {
            Future<String>ft = service.submit(new HtmlPageProducer(htmlPageQueue,cookieStore,i) {
            });
            producerFutures.add(ft);
        }
        for (int i = 0; i <5 ; i++) {
            Future<String>ft = service.submit(new HtmlPageProcessor(htmlPageQueue,manualWritingQueue) {
            });
            processorFutures.add(ft);
        }
        new Thread(new ManualToFileWriter(manualWritingQueue)).start();
        while (isRunning(producerFutures)){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("writer started");
        try {
            htmlPageQueue.put(TOXIC_WORD);
        } catch (InterruptedException ignored) {
        }
        while (isRunning(processorFutures)){
            try {
                System.out.println("processors running");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
        try {
            manualWritingQueue.put(TOXIC_LIST);
        } catch (InterruptedException ignored) {
        }
        service.shutdown();
    }


    public static boolean isRunning(List<Future> list){
        for (Future future : list){
            if (!future.isDone())return true;
        }
        return false;
    }
}
