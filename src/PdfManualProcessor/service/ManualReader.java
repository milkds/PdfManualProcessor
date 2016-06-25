package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.StringWriter;

public class ManualReader {
    private final static String PDF_STORAGE_DIR = "D:\\pdf.Storage\\";
    final static int NUMBER_OF_PAGES_TO_READ = 5;

    /**
     * Method from old project. To be updated.
     * FileStorageAddress Should be stated in Properties. Temporary we will use String variable.
     */
    private static String readStartingPages(Manual manual, int pagesQuantity) {
        String text;
        StringWriter fileBody = new StringWriter();
        try {
            PdfReader pdfReader = new PdfReader(PDF_STORAGE_DIR + manual.getId() + ".pdf");
            int totalNumberOfPages = pdfReader.getNumberOfPages();
            if (pagesQuantity > totalNumberOfPages) pagesQuantity = totalNumberOfPages;
            for (int i = 1; i < pdfReader.getNumberOfPages() && i < pagesQuantity; i++) {
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                text = PdfTextExtractor.getTextFromPage(pdfReader, i, strategy);
                fileBody.write(text);
            }
            pdfReader.close();
        }
        //to be reworked
        catch (Exception | Error e) {
            System.out.println("PDF file is not readable");
        }

        return fileBody.toString();
    }
    public static String readStartingPages(Manual manual){
        return readStartingPages(manual,NUMBER_OF_PAGES_TO_READ);
    }


    //todo: rework catch section from readStartingPages

}
