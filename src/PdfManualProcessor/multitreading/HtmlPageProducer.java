package PdfManualProcessor.multitreading;

import PdfManualProcessor.service.LoginHandler;
import org.apache.http.client.CookieStore;

import java.util.concurrent.BlockingQueue;

public class HtmlPageProducer implements Runnable{
    /**
     * This class gets HTML pages with up to 10 manual links on it.
     */

    private final BlockingQueue<String> queue;
    private final CookieStore cookieStore;
    private final int pageNo;

    public HtmlPageProducer(BlockingQueue<String> queue, CookieStore cookieStore, int pageNo) {
        this.queue = queue;
        this.cookieStore = cookieStore;
        this.pageNo = pageNo;
    }

    @Override
    public void run() {
        String pageBody = LoginHandler.getHtmlPage(cookieStore,pageNo);
        try {
            queue.put(pageBody);
        }
        catch (InterruptedException ignored) {
        }
    }
}
