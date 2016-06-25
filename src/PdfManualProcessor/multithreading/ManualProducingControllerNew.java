package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.LoginHandler;
import PdfManualProcessor.service.ManualPageParser;
import PdfManualProcessor.service.ManualSerializer;
import PdfManualProcessor.view.LongActionProgressBar;
import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ManualProducingControllerNew {
    private DownloadController downloadController;
    private ManualFilteringController filterController;

    public static void main(String[] args) throws InterruptedException, IOException {

    }

    public static void refreshManualList() throws IOException {
        List<Manual> temp = new ArrayList<>();
        CookieStore cookieStore = LoginHandler.getCookies("","");
        int totalManuals = ManualPageParser.getManualsQuantity(LoginHandler.getHtmlPage(cookieStore,1));
        System.out.println(totalManuals);
        int totalPages = totalManuals/10;
        if (totalManuals%10>0)totalPages++;
        System.out.println(totalPages);
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 1; i <=totalPages ; i++) {
            service.submit(new HtmlPageProducer(temp,cookieStore,i));
        }
        while (temp.size()<totalManuals){
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ignored) {
            }
        }
        List<Manual> allManuals = ManualSerializer.getAllManualsFromFile();
        temp.removeAll(allManuals);
        allManuals.addAll(temp);
        Collections.sort(allManuals);
        ManualSerializer.refreshRawManualFile(allManuals);
        System.out.println("all Manuals are up-to-date");
    }

    public static void deleteManualsInConsole(List<Manual> manuals) {
        for (final Manual m : manuals){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LoginHandler.removeManualInConsole(m);
                }
            }).start();
        }
    }

    public void downloadManuals(LongActionProgressBar progressBar){
        downloadController = new DownloadController();
        downloadController.downloadManuals();
        progressBar.setCounter(downloadController.getCounter());
        progressBar.setTotal(downloadController.getTotal());
        new Thread(progressBar).start();
    }
    public void cancelDownloadManuals(){
        downloadController.cancelDownload();
    }
    public void cancelManualFiltration(){
        filterController.cancelFiltration();
    }

    public void filterManuals(LongActionProgressBar progressBar) {
        filterController = new ManualFilteringController();
      //  filterController.filterManualsByUrl();
        filterController.filterManualsByBody();
        progressBar.setCounter(filterController.getCounter());
        progressBar.setTotal(filterController.getTotal());
        new Thread(progressBar).start();


    }
}
