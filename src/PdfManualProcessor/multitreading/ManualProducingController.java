package PdfManualProcessor.multitreading;

import PdfManualProcessor.Manual;
import org.apache.http.client.CookieStore;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ManualProducingController {
    static final String TOXIC_WORD = "toxic";
    static final Integer TOXIC_SIZE = 20;

    public static void main(String[] args) {


    }

    public static void getManuals(int startPage, int finishPage, CookieStore cookieStore){
        BlockingQueue<String> htmlPageQueue = new LinkedBlockingQueue();
        BlockingQueue<List<Manual>> manualWritingQueue = new LinkedBlockingQueue();
        for (int i = startPage; i <finishPage ; i++) {
            new Thread(new HtmlPageProducer(htmlPageQueue,cookieStore,i)).start();
        }
        for (int i = 0; i <10 ; i++) {
            new Thread(new HtmlPageProcessor(htmlPageQueue,manualWritingQueue)).start();
        }
        try {
            Thread.sleep(5000);//to be reworked - need to find moment when all HtmlPageProducer threads finished.
            htmlPageQueue.put(TOXIC_WORD);
        } catch (InterruptedException ignored) {

        }
    }
}
