package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class gets HTML page from LoginHandler and Parses it on up to 10 Manuals. Returns List<Manual> manuals.
 */
public class ManualPageParser {

    public static List<Manual> getManuals(String pageBody){
        List<Manual> result = new ArrayList<>();
        Document doc = Jsoup.parse(pageBody);
        for (Element element : doc.getElementsByAttribute("name")){
            if(Objects.equals(element.attr("name"), "url")) {
                String id = element.attr("id").substring(3);
                String pdfUrl = element.attr("value");
                result.add(new Manual(id, pdfUrl,0));
            }
        }
        return result;
    }
    public static int getManualsQuantity(String pageBody){
        Document doc = Jsoup.parse(pageBody);
        String total; //total manuals for User.
        String done; //total manuals done by User
        String s; //just for easy substring extract
        List<Element> elements = doc.getElementsByClass("done");
        s = elements.get(1).text();
        total = s.substring(s.indexOf(" ")+1);
        done = elements.get(2).getElementsByAttribute("id").text();

        return Integer.parseInt(total)-Integer.parseInt(done);
    }


    // TODO:  decide and implement methods
}
