package PdfManualProcessor.multitreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.LoginHandler;
import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ManualProducingController {
    static final String TOXIC_WORD = "toxic";
    static final List<Manual> TOXIC_LIST = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i <11 ; i++) {
            TOXIC_LIST.add(new Manual("","",0));
        }
        try {
            getManuals(50,52, LoginHandler.getCookies("LOGIN","PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        new Thread(new ManualToFileWriter(manualWritingQueue)).start();
        System.out.println("writer started");
        try {
            Thread.sleep(5000);//to be reworked - need to find moment when all HtmlPageProducer threads finished.
            htmlPageQueue.put(TOXIC_WORD);
        } catch (InterruptedException ignored) {

        }
    }
}
