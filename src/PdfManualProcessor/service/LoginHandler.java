package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for Login and Obtaining raw files data.
 */
public class LoginHandler {
    private static final String MANUALS_PAGE_URL = "http://74.117.180.69:83/work/pdfapprove/index.php?page=";
    private static final String LOGIN_PAGE_URL = "http://74.117.180.69:83/work/pdfapprove/index.php?action=login";
    private static final String CHANGE_STATE_URL = "http://74.117.180.69:83/work/pdfapprove/model/set_state.php";

    public static CookieStore getCookies(String login, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        HttpPost httpPost = new HttpPost(LOGIN_PAGE_URL);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("auth", "1"));
        urlParameters.add(new BasicNameValuePair("login", login));
        urlParameters.add(new BasicNameValuePair("password", password));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        httpclient.execute(httpPost,context);
        CookieStore cookieStore = context.getCookieStore();
        httpclient.close();
        return cookieStore;
    }
    public static String getHtmlPage(CookieStore cookieStore,int pageNo) /*throws IOException*/ {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        String url = MANUALS_PAGE_URL+pageNo;
        HttpGet httpGet = new HttpGet(url);
        StringBuilder result = new StringBuilder();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpGet,context);
            String line;
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
            rd.close();
            httpclient.close();
        } catch (IOException e) {
          return "";
        }
        return result.toString();
    }

    public static void removeManualInConsole(Manual manual){
        CookieStore cookieStore=null;
        try {
            cookieStore = getCookies("login","password");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        HttpPost httpPost = new HttpPost(CHANGE_STATE_URL);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("id", manual.getId()));
        urlParameters.add(new BasicNameValuePair("delete", "delete"));
        urlParameters.add(new BasicNameValuePair("user", "user_fl7")); //need to implement getting value here

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            httpclient.execute(httpPost,context);
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


 //// TODO: 04.04.2016 add JavaDocs. Implement Exception handling. Implement getting value for manual delete method.
}
