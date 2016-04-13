package PdfManualProcessor.service;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.StringWriter;

public class ManualReader {

    /**
     * Method from old project. To be updated.
     * @param fileName
     * @return
     */
    public static String readPdf(String fileName){
        String text;
        StringWriter sb = new StringWriter();
        try {
            PdfReader pdfReader = new PdfReader(fileName);
            pdfReader.getInfo();

            for (int i = 1; i <= pdfReader.getNumberOfPages()&&i<=2; ++i) {
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                text = PdfTextExtractor.getTextFromPage(pdfReader, i, strategy);
                sb.write(text);

            }
            pdfReader.close();
            //   System.out.println("file - "+fileName + " was read successfully.");
        } catch (Exception e) {
            System.out.println("exception occured while reading file - " + fileName);
        }
        catch (Error e){
            System.out.println("error occured while reading file - " + fileName);
        }

        return sb.toString();
    }
}
