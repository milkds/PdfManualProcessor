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

/**
 * This class manages all multithreading functions (downloading, filtration, etc.)
 */
public class ManualProducingController {
    private DownloadController downloadController;
    private ManualFilteringController filterController;

    /**
     * Refreshes list of all manuals.
     * @throws IOException
     */
    public static void refreshManualList() throws IOException {
        //Making container for all manuals in system.
        List<Manual> temp = new ArrayList<>();

        //Getting cookieStore to avoid further cookie requests.
        CookieStore cookieStore = LoginHandler.getCookies("LOGIN","PASSWORD");

        //Total quantity of manuals in system (parsing from start page).
        int totalManuals = ManualPageParser.getTotalManualsQuantityLeftForProcessing(LoginHandler.getHtmlPage(cookieStore,1));

        //Total full html pages in system (each page contains 10 manuals)
        int totalPages = totalManuals/10;

        //Total html pages in system (if last contains less then 10 manuals)
        if (totalManuals%10>0)totalPages++;

        System.out.println("total pages = "+ totalPages+". Total manuals - " + totalManuals);

        //Creating and starting tasks to get html pages body. If start all them at once - server will fail to response in 30% times.
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 1; i <=totalPages ; i++) {
            service.submit(new HtmlPageProducer(temp,cookieStore,i));
        }

        //Checking until we get all manuals.
        while (temp.size()<totalManuals){
            try {
                System.out.println(temp.size());
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ignored) {
            }
        }

        //Sorting list of manuals by domain of their URLs.
        Collections.sort(temp);

        //Serialising our list of manuals.
        ManualSerializer.refreshRawManualFile(temp);
        System.out.println("all Manuals are up-to-date");
    }

    /**
     * Deletes manuals in system.
     * @param manuals - list of manuals for delete.
     */
    public static void deleteManualsInConsole(List<Manual> manuals) {
        //for each manual we start new thread, which sends delete request to server.
        for (final Manual m : manuals){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LoginHandler.removeManualInConsole(m);
                }
            }).start();
        }
    }

    /**
     * Calls manual downloading start and also starts progress bar thread.
     * @param progressBar - progress bar for LongActionView class.
     */
    public void downloadManuals(LongActionProgressBar progressBar){
        //initialising downloadController.
        downloadController = new DownloadController();

        //starting download.
        downloadController.downloadManuals();

        //initialising and starting progress bar.
        progressBar.setCounter(downloadController.getCounter());
        progressBar.setTotal(downloadController.getTotal());
        new Thread(progressBar).start();
    }

    /**
     * Cancels manual downloading.
     */
    public void cancelDownloadManuals(){
        downloadController.cancelDownload();
    }

    /**
     * Cancels manual filtration.
     */
    public void cancelManualFiltration(){
        filterController.cancelFiltration();
    }

    /**
     * Calls manual filtration start and also starts progress bar thread.
     * @param progressBar - progress bar for LongActionView class.
     */
    public void filterManuals(LongActionProgressBar progressBar) {
        //initialising filtration controller.
        filterController = new ManualFilteringController();

        //filtering by Url.
        filterController.filterManualsByUrl();

        //filtering by manual file body (method starts filtering procedure in separate thread, so we
        //go further at this method almost immediately.
        filterController.filterManualsByBody();

        //initialising and starting progress bar for filtration by body (filtration by url is not a long action).
        progressBar.setCounter(filterController.getCounter());
        progressBar.setTotal(filterController.getTotal());
        new Thread(progressBar).start();
    }

    //// TODO: 29.06.2016 refactor deleteManualsInConsole() method to make it use ExecutorService. Name this class properly.
    //// TODO: 29.06.2016 Remove hardcode from refreshManualList() method (in block where we check if refresh process is over or not).
    //// TODO: 29.06.2016 Move progress bar initialisation to separate method.
}
