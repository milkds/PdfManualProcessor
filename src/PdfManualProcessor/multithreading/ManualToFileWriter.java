package PdfManualProcessor.multithreading;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualSerializer;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ManualToFileWriter implements Runnable {
    private final BlockingQueue<List<Manual>> manualWritingQueue;
    private final Path serializationFilePath;

    public ManualToFileWriter(BlockingQueue<List<Manual>> manualWritingQueue, Path serializationFilePath) {
        this.manualWritingQueue = manualWritingQueue;
        this.serializationFilePath = serializationFilePath;
    }

    @Override
    public void run() {
        while (true){
            List<Manual> manuals = null;
            try{
                manuals = manualWritingQueue.take();
                if (manuals==null){
                    System.out.println("got toxic list");//remove lately
                   break;
                }
                ManualSerializer.saveManualsToFile(manuals,serializationFilePath);
                System.out.println("manuals saved");
             }
           catch (InterruptedException e){
               break;
           }
        }
    }
}
