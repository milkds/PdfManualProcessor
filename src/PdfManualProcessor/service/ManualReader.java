package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.StringWriter;

public class ManualReader {
    private final static String PDF_STORAGE_DIR = "D:\\pdf.Storage\\";

    /**
     * Method from old project. To be updated.
     * FileStorageAdress Should be stated in Properties. Temporary we will use String variable.
     */
    public static String readPdf(Manual manual){
        String text;
        StringWriter fileBody = new StringWriter();
        try {
            PdfReader pdfReader = new PdfReader(PDF_STORAGE_DIR+manual.getId()+".pdf");


            for (int i = 1; i <= pdfReader.getNumberOfPages()&&i<=2; ++i) {
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                text = PdfTextExtractor.getTextFromPage(pdfReader, i, strategy);
                fileBody.write(text);

            }
            pdfReader.close();
            //   System.out.println("file - "+fileName + " was read successfully.");
        } catch (Exception | Error e) {
            System.out.println("error");
        }

        return fileBody.toString();
    }
}
