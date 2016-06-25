package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.DictionaryHandler;
import PdfManualProcessor.service.ManualFilter;
import PdfManualProcessor.service.ManualSerializer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ManualFilteringController {
    private final BlockingQueue<Manual> filteringQueue = new LinkedBlockingQueue<>();;
    private static final int THREAD_QUANTITY = 5;

    public Integer getTotal() {
        return total;
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    private Integer total;
    private AtomicInteger counter;
    public void filterManualsByUrl(){
        List<Manual> manuals = ManualSerializer.getManualsForFiltration();

        List<String> sureDeleteDictionary = DictionaryHandler.getSureDeleteByUrlDictionary();
        List<String> checkDeleteDictionary = DictionaryHandler.getCheckDeleteByUrlDictionary();

        ManualFilter.filterManualsByUrl(manuals,sureDeleteDictionary,checkDeleteDictionary);
    }

    public void filterManualsByBody(){
        List<Manual> manuals = ManualSerializer.getManualsForFiltration();
        for (Manual manual : manuals){
            try {
                filteringQueue.put(manual);
            } catch (InterruptedException ignored) {
            }
        }
        total = manuals.size();
        counter = new AtomicInteger(0);
        List<String> sureDeleteDictionary = DictionaryHandler.getSureDeleteByBodyDictionary();
        List<String> checkDeleteDictionary = DictionaryHandler.getCheckDeleteByBodyDictionary();

        for (int i = 0; i < THREAD_QUANTITY ; i++) {
           new Thread(new ManualByBodyFilter(counter, total, filteringQueue,sureDeleteDictionary,checkDeleteDictionary)).start();
        }

    }

    public void cancelFiltration(){
        synchronized (filteringQueue){
            filteringQueue.clear();
        }
    }
}
