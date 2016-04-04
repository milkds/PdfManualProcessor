package PdfManualProcessor.service;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for Login and Obtaining raw files data.
 */
public class LoginHandler {


    public static CookieStore getCookies(String login, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        HttpPost httpPost = new HttpPost("http://74.117.180.69:83/work/pdfapprove/index.php?action=login");
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("auth", "1"));
        urlParameters.add(new BasicNameValuePair("login", login));
        urlParameters.add(new BasicNameValuePair("password", password));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse httpResponse = httpclient.execute(httpPost,context);
        CookieStore cookieStore = context.getCookieStore();
        httpclient.close();
        return cookieStore;
    }
    public static String getHtmlPage(CookieStore cookieStore,int pageNo) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        String url = "http://74.117.180.69:83/work/pdfapprove/index.php?page="+pageNo;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse2 = httpclient.execute(httpGet,context);
        BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse2.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
            result.append("\n");
        }
        rd.close();

        httpclient.close();
        return result.toString();
    }


}
