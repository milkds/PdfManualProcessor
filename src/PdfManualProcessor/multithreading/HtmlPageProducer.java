package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.LoginHandler;
import PdfManualProcessor.service.ManualPageParser;
import org.apache.http.client.CookieStore;

import java.util.List;

public class HtmlPageProducer implements Runnable{
    /**
     * This class gets HTML pages with up to 10 manual links on it.
     */

    private final List<Manual> tmpManualList;
    private final CookieStore cookieStore;
    private final int pageNo;

    public HtmlPageProducer(List<Manual> tmpManualList, CookieStore cookieStore, int pageNo) {
        this.tmpManualList = tmpManualList;
        this.cookieStore = cookieStore;
        this.pageNo = pageNo;
    }

    @Override
    public void run() {
        String pageBody = null;
        while (pageBody==null||pageBody.length()<1024){
            pageBody = LoginHandler.getHtmlPage(cookieStore,pageNo);
        }
        List<Manual> manuals = ManualPageParser.getManuals(pageBody);
        synchronized (tmpManualList){
           tmpManualList.addAll(manuals);
        }
        System.out.println(pageNo + "page is processed.");
    }
}
