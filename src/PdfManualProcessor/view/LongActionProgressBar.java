package PdfManualProcessor.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is progress bar for user notification purposes.
 */
public class LongActionProgressBar extends JProgressBar implements Runnable {

    private AtomicInteger counter=new AtomicInteger(0); //this is used to count % of work complete.
    private Integer total=0; //this is used to count % of work complete.
    private TitledBorder border; //For view purposes.

    public LongActionProgressBar() {
        init();
    }

    @Override
    public void run() {
        //Checking if work already started and there is work to do. Don't start working, until work is started.
        //we start this thread only after total and counter get actual values. Possibly it is no sense for it - need to check.
        while (counter.intValue()==0||total==0){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        //Initialising progress variable.
        int progress =0;

        //Updating progress.
        while (progress<100){
            progress = counter.intValue()*100/total;
            setValue(progress);
        }

        //Changing view to notify user, after work is done.
        border.setTitle("Finished.");
        revalidate();
        repaint();
    }

    /**
     * Initialises progress bar.
     */
    private void init(){
        //Creating border.
        border = BorderFactory.createTitledBorder("Processing...");
        setBorder(border);

        //Setting up progress bar.
        setValue(0);
        setStringPainted(true);
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    public void setCounter(AtomicInteger counter) {
        this.counter = counter;
    }
}
