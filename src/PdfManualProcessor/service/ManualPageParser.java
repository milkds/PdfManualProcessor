package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
                result.add(new Manual(id, pdfUrl));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Manual> manuals = new ArrayList<>();
        try {
        manuals=getManuals(LoginHandler.getHtmlPage(LoginHandler.getCookies("LOGIN","PASSWORD"),10));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Manual m : manuals){
            System.out.println(m.getPdfUrl()+" "+ m.getId()+" "+ m.getSize());
        }
    }

    // TODO:  decide and implement methods
}
