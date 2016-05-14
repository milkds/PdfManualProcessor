package PdfManualProcessor.multithreading;

import PdfManualProcessor.Controller;
import PdfManualProcessor.Manual;
import PdfManualProcessor.service.LoginHandler;
import PdfManualProcessor.service.ManualPageParser;
import PdfManualProcessor.service.ManualSerializer;
import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ManualProducingControllerNew {

    public static void main(String[] args) throws InterruptedException, IOException {
       // refreshManualList();
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

    public static void refreshManualList(Controller controller) throws IOException {
        List<Manual> temp = new ArrayList<>();
        CookieStore cookieStore = LoginHandler.getCookies("","");
        int totalManuals = ManualPageParser.getManualsQuantity(LoginHandler.getHtmlPage(cookieStore,1));
        int totalPages = totalManuals/10;  //implement according method
        if (totalManuals%10>0)totalPages++;
        for (int i = 1; i <=totalPages ; i++) {
            new Thread(new HtmlPageProducer(temp,cookieStore,i)).start();
        }
        while (temp.size()<totalManuals){
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ignored) {
            }
        }
        List<Manual> allManuals = ManualSerializer.getManualsFromFile();
        temp.removeAll(allManuals);
        ManualSerializer.saveRawManualsToFile(temp);
        System.out.println("all Manuals are up-to-date");
    }
}
