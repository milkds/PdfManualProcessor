package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualFilter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ManualByBodyFilter implements Runnable {

    private final AtomicInteger counter;
    private  Integer total;
    private BlockingQueue<Manual> filteringQueue;
    private List<String> sureDeleteDictionary;
    private List<String> checkDeleteDictionary;

    private static final String NOTIFY_FORMAT = "checked %d manuals of total %d in %d ms. %s - %s";

    public ManualByBodyFilter(AtomicInteger counter, Integer total, BlockingQueue<Manual> filteringQueue, List<String> sureDeleteDictionary, List<String> checkDeleteDictionary) {
        this.counter = counter;
        this.total = total;
        this.filteringQueue = filteringQueue;
        this.sureDeleteDictionary = sureDeleteDictionary;
        this.checkDeleteDictionary = checkDeleteDictionary;
    }

    @Override
    public void run() {
        while (true){
            Long start=System.currentTimeMillis();
            Manual m = null;
            try {
                 m = filteringQueue.take();
                if (m==null){
                    filteringQueue.put(m);
                    break;
                }
            } catch (InterruptedException ignored) {
            }
            ManualFilter.filterManualByBody(m,sureDeleteDictionary,checkDeleteDictionary);

            notifyUser(m,start,System.currentTimeMillis());
        }
    }

    private void notifyUser(Manual m, Long start, Long finish){
        synchronized(counter) {
            System.out.println(String.format(NOTIFY_FORMAT, counter.incrementAndGet(), total, (finish - start), m.getPdfUrl(), m.getId()));
        }
    }
}
