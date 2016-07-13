package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.LoginHandler;
import PdfManualProcessor.service.ManualPageParser;
import org.apache.http.client.CookieStore;

import java.util.List;

public class HtmlPageProducer implements Runnable{
    /**
     * This class transforms HTML pages with up to 10 manual links on it to list of Manuals.
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
        //Initialising String variable, for keeping body of Html page.
        String pageBody = "";

        //getting page body. Retrying till we get valid body.
        while (pageBody.length()<1024){
            pageBody = LoginHandler.getHtmlPage(cookieStore,pageNo);
        }

        //parsing page body on up to 10 Manual objects with 0 size.
        List<Manual> manuals = ManualPageParser.getManuals(pageBody);

        //writing Manuals to common List<Manual>.
        synchronized (tmpManualList){
           tmpManualList.addAll(manuals);
        }

        System.out.println("got page " + pageNo);
    }
}
