package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class gets HTML page and parses it to get required information.
 */
public class ManualPageParser {
    /**
     * Gets List of manuals from html page.
     * @param pageBody Body of html page.
     * @return List of manuals from html page.
     */
    public static List<Manual> getManualsList(String pageBody){
        List<Manual> result = new ArrayList<>();

        //Parsing page.
        Document doc = Jsoup.parse(pageBody);

        //Getting manuals.
        for (Element element : doc.getElementsByAttribute("name")){
            if(Objects.equals(element.attr("name"), "url")) {

                //Getting manual id.
                String id = element.attr("id").substring(3);

                //Getting manual url.
                String pdfUrl = element.attr("value");

                //Creating new manual object and adding it to result list.
                result.add(new Manual(id, pdfUrl,0));
            }
        }
        return result;
    }

    /**
     * Gets total manual quantity in system from start page.
     * @param pageBody Start page body.
     * @return Total number of manuals to be processed by user.
     */
    public static int getTotalManualsQuantityLeftForProcessing(String pageBody){
        //Parsing page.
        Document doc = Jsoup.parse(pageBody);

        String total; //total manuals for User.
        String done; //total manuals done by User.
        String s; //just for easy substring extract.

        //Getting array of information, which includes the number we search for.
        List<Element> elements = doc.getElementsByClass("done");

        //Getting element, which contains total quantity of manuals for current users account.
        s = elements.get(1).text();
        total = s.substring(s.indexOf(" ")+1);

        //Getting element, which contains total quantity of manuals already processed by user.
        done = elements.get(2).getElementsByAttribute("id").text();

        //Converting both numbers to Integer and getting difference between them - which is the number we search for.
        return Integer.parseInt(total)-Integer.parseInt(done);
    }
}
