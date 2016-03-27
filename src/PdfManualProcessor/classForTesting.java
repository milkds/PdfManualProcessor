package PdfManualProcessor; /**
 * This class is for testing, during project. To be deleted afterwards.
 */

import PdfManualProcessor.service.LoginHandler;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Objects;

public class ClassForTesting {
    static final String URL = "http://74.117.180.69:83/work/pdfapprove/index.php";

    public static void main(String[] args)
    {
      Document doc = getHtmlDocument(5,"login","password");
        for (Element element : doc.getElementsByAttribute("name")){
            if(Objects.equals(element.attr("name"), "url")) {
                // System.out.println(element.attr("value"));
                System.out.println(element.attr("id"));
            }

        }

    }

    protected static Document getHtmlDocument(int pageNo,String login, String password) {
        Document doc = null;
        try{
          doc = Jsoup.parse(LoginHandler.getHtmlPage(pageNo,login,password));
        } catch (IOException e) {e.printStackTrace();}
        return doc;
    }
}
