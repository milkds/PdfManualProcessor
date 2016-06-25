package PdfManualProcessor.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LongActionProgressBar extends JProgressBar implements Runnable {

    private AtomicInteger counter=new AtomicInteger(0);
    private Integer total=0;

    private TitledBorder border;

    public LongActionProgressBar() {
        init();
    }

    @Override
    public void run() {
        //we start this thread only after total and counter get actual values. Possibly it is no sense for it - need to check.
        while (counter.intValue()==0||total==0){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
        int progress =0;
        while (progress<100){
            progress = counter.intValue()*100/total;
            setValue(progress);
        }
        border.setTitle("Finished.");
        revalidate();
        repaint();
    }

    private void init(){
        border = BorderFactory.createTitledBorder("Processing...");

        setValue(0);
        setStringPainted(true);
        setBorder(border);
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    public void setCounter(AtomicInteger counter) {
        this.counter = counter;
    }
}
