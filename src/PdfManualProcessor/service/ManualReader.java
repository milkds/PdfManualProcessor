package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.StringWriter;

/**
 * This class reads manual pdf file from disk and returns its first pages.
 */
public class ManualReader {
    private final static String PDF_STORAGE_DIR = "D:\\pdf.Storage\\";
    final static int NUMBER_OF_PAGES_TO_READ = 5;


    /**
     * Method from old project. To be updated.
     * FileStorageAddress Should be stated in Properties. Temporary we use String variable.
     * @param manual - manual to read.
     * @param pagesQuantity - quantity of pages to read. (not necessary to read full
     *                      pdf file (especially 100+ pages), as keywords are usually met on first 3-4 pages.
     * @return
     */
    private static String readStartingPages(Manual manual, int pagesQuantity) {
        //Preparing container for manual body.
        String text;
        StringWriter fileBody = new StringWriter();

        //Reading file, throwing exception/error if file is corrupted.
        try {
            //initialising reader.
            PdfReader pdfReader = new PdfReader(PDF_STORAGE_DIR + manual.getId() + ".pdf");

            //getting total number of pages in manual.
            int totalNumberOfPages = pdfReader.getNumberOfPages();

            //Checking if manual has less pages than we intend to read. This needed to avoid NPE.
            if (pagesQuantity > totalNumberOfPages) pagesQuantity = totalNumberOfPages;

            //Reading pages and adding text from them to container.
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

    /**
     * Calls private method to read defined quantity of pages.
     * @param manual - manual, which body we are trying to read.
     * @return manuals body.
     */
    public static String readStartingPages(Manual manual){
        return readStartingPages(manual,NUMBER_OF_PAGES_TO_READ);
    }


    //todo: rework catch section from readStartingPages.
    //todo: try to implement pdf reading via IcePDF lib and check which is better.

}
