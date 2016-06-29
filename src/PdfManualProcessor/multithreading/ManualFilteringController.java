package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.DictionaryHandler;
import PdfManualProcessor.service.ManualFilter;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class manages Manual filtration.
 */
public class ManualFilteringController {
    private final BlockingQueue<Manual> filteringQueue = new LinkedBlockingQueue<>();;
    private static final int THREAD_QUANTITY = 5;



    private Integer total;
    private AtomicInteger counter;

    /**
     * Filtering manuals by URL.
     */
    public void filterManualsByUrl(){
        //getting list of manuals for filtration.
        List<Manual> manuals = ManualSerializer.getManualsForFiltration();

        //Initialising dictionaries.
        List<String> sureDeleteDictionary = DictionaryHandler.getSureDeleteByUrlDictionary();
        List<String> checkDeleteDictionary = DictionaryHandler.getCheckDeleteByUrlDictionary();

        //filtering manuals.
        ManualFilter.filterManualsByUrl(manuals,sureDeleteDictionary,checkDeleteDictionary);
    }

    /**
     * Filtering manuals by their bodies.
     */
    public void filterManualsByBody(){
        //getting manuals for filtration.
        List<Manual> manuals = ManualSerializer.getManualsForFiltration();

        //filling queue.
        for (Manual manual : manuals){
            try {
                filteringQueue.put(manual);
            } catch (InterruptedException ignored) {
            }
        }

        //initialising filtered manual counter and total quantity of manuals for filtration variable.
        total = manuals.size();
        counter = new AtomicInteger(0);

        //initialising dictionaries
        List<String> sureDeleteDictionary = DictionaryHandler.getSureDeleteByBodyDictionary();
        List<String> checkDeleteDictionary = DictionaryHandler.getCheckDeleteByBodyDictionary();

        //Launching filtering threads,
        for (int i = 0; i < THREAD_QUANTITY ; i++) {
           new Thread(new ManualByBodyFilter(counter, total, filteringQueue,sureDeleteDictionary,checkDeleteDictionary)).start();
        }

    }

    /**
     * Cancels filtration by clearing queue.
     */
    public void cancelFiltration(){
        synchronized (filteringQueue){
            filteringQueue.clear();
        }
    }
    public Integer getTotal() {
        return total;
    }
    public AtomicInteger getCounter() {
        return counter;
    }

    //todo: Decide quantity of threads necessary and where to store this variable.
}
