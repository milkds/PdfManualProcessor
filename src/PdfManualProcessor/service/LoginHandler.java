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
 * This class manages networking between client and system.
 */
public class LoginHandler {
    private static final String MANUALS_PAGE_URL = "http://74.117.180.69:83/work/pdfapprove/index.php?page=";
    private static final String LOGIN_PAGE_URL = "http://74.117.180.69:83/work/pdfapprove/index.php?action=login";
    private static final String CHANGE_STATE_URL = "http://74.117.180.69:83/work/pdfapprove/model/set_state.php";

    /**
     * Gets cookies for further requests to system.
     *
     * @param login - user Login.
     * @param password - user Password.
     * @return CookieStore
     * @throws IOException
     */
    public static CookieStore getCookies(String login, String password) throws IOException {
        //Creating new httpClient and context.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();

        //Creating HttpPost request.
        HttpPost httpPost = new HttpPost(LOGIN_PAGE_URL);

        //Adding necessary parameters to HttpPost request.
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("auth", "1"));
        urlParameters.add(new BasicNameValuePair("login", login));
        urlParameters.add(new BasicNameValuePair("password", password));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        //Sending request.
        httpclient.execute(httpPost,context);

        //Getting cookies.
        CookieStore cookieStore = context.getCookieStore();

        //Closing httpClient.
        httpclient.close();

        return cookieStore;
    }

    /**
     * Gets html page body for parsing on manual objects.
     * @param cookieStore - cookies, necessary to get requested page.
     * @param pageNo - page number in system.
     * @return html page body in String variable.
     */
    public static String getHtmlPage(CookieStore cookieStore,int pageNo) {
        //Creating new httpClient and context.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();

        //Adding cookies to context.
        context.setCookieStore(cookieStore);

        //Getting url for httpGet request.
        String url = MANUALS_PAGE_URL+pageNo;

        //Building httpGet request
        HttpGet httpGet = new HttpGet(url);

        //Getting storage for html page body.
        StringBuilder result = new StringBuilder();

        //Executing httpGet request. Writing html page body to StringBuilder.
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
        } catch (IOException ignored) {
          return "";
        }
        return result.toString();
    }

    /**
     * Send delete request to system for selected manual.
     * @param manual - manual to delete.
     */
    public static void removeManualInConsole(Manual manual, CookieStore cookieStore){
        //Building httpClient and context.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();

        //Adding cookies to context.
        context.setCookieStore(cookieStore);

        //Building post request.
        HttpPost httpPost = new HttpPost(CHANGE_STATE_URL);

        //Adding necessary parameters to httpPost request
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("id", manual.getId()));
        urlParameters.add(new BasicNameValuePair("delete", "delete"));
        urlParameters.add(new BasicNameValuePair("user", "user_fl7"));

        //Executing request, closing httpClient.
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            httpclient.execute(httpPost,context);
            httpclient.close();
            System.out.println(manual.getId()+" manual was deleted successfully.");
        } catch (IOException ignored) {
        }
    }
}
