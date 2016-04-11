package PdfManualProcessor; /**
 * This class is for testing, during project. To be deleted afterwards.
 */

import PdfManualProcessor.service.LoginHandler;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassForTesting {
    static final String URL = "http://74.117.180.69:83/work/pdfapprove/index.php";

    public static void main(String[] args)
    {

    }

    protected static Document getHtmlDocument(int pageNo,String login, String password) {
        Document doc = null;
        try{
          doc = Jsoup.parse(LoginHandler.getHtmlPage(LoginHandler.getCookies(login,password),pageNo));
        } catch (IOException e) {e.printStackTrace();}
        return doc;
    }
    public static void testParse(){
        Document doc = getHtmlDocument(5,"login","password");
        for (Element element : doc.getElementsByAttribute("name")){
            if(Objects.equals(element.attr("name"), "url")) {
                // System.out.println(element.attr("value"));
                System.out.println(element.attr("id"));
            }
        }
    }

    public static void someArrayTesting(){
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1.add("A");
        list1.add("B");
        list1.add("C");
        list2.add("D");
        list2.add("E");
        list2.add("C");
        list1.removeAll(list2);
        System.out.println(list1);
        System.out.println(list2);
    }
}
