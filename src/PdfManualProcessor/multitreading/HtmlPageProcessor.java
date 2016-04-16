package PdfManualProcessor.multitreading;

import java.util.concurrent.BlockingQueue;

public class HtmlPageProcessor implements Runnable{

    private final BlockingQueue queue;

    public HtmlPageProcessor(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

    }
}
