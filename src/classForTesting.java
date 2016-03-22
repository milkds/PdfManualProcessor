/**
 * This class is for testing, during project. To be deleted afterwards.
 */



import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class classForTesting {
    static final String URL = "http://74.117.180.69:83/work/pdfapprove/index.php";

    public static void main(String[] args)
    {
     //   login();
      Document doc = new classForTesting().getHtmlDocument();
    }



    protected Document getHtmlDocument() {
        Document doc = null;
        try{
          doc = Jsoup.connect(URL).get();
        } catch (IOException e) {e.printStackTrace();}
        return doc;
    }
}
