package PdfManualProcessor; /**
 * This class is for testing, during project. To be deleted afterwards.
 */

import PdfManualProcessor.service.LoginHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class classForTesting {
    static final String URL = "http://74.117.180.69:83/work/pdfapprove/index.php";

    public static void main(String args[]) throws InterruptedException {
        JFrame f = new JFrame("JProgressBar Sample");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = f.getContentPane();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Reading...");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        f.setSize(300, 100);
        f.setVisible(true);
        for (int i = 1; i <=100; i++) {
            TimeUnit.MILLISECONDS.sleep(50);
            progressBar.setValue(i);
        }
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
