package PdfManualProcessor.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ManualSizeChecker {
    private static final String PROXY_URL = "http://74.117.180.69:83/work/pdfapprove/get_pdf_curl.php?url=";

    public static int getManualSize(String urlString){
        if (urlString.toLowerCase().startsWith("ftp"))return getSizeByFtp(urlString);
        else return getSizeByHttp(urlString);
    }

    private static int getSizeByFtp(String urlString){
        int result =0;
        FTPClient ftpClient = new FTPClient();
        URL url = null;
        try {
            url = getUrlForFtp(urlString);
            ftpClient.connect(url.getHost());
            ftpClient.login("anonymous","user@domain.net");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.sendCommand("SIZE", url.getPath());
            if(ftpClient.getReplyCode()==213) {
                String reply = ftpClient.getReplyString();
                result = Integer.parseInt(reply.substring(reply.lastIndexOf(" ")).trim());
            }
        }
        catch (IOException ignored) {
        }
        return result;
    }
    private static int getSizeByHttp(String urlString){
        return getSizeByHttp(getHttpResponse(urlString));
    }
    private static int getSizeByHttp(HttpResponse response){
        int result =0;
        if (response==null)return result;
        Header header = response.getFirstHeader("Content-Length");
        if(header!=null){
            result = Integer.parseInt(header.getValue());
        }
        return result;
    }
    private static HttpResponse getHttpResponse(String urlString){
        System.out.println("get response method");
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response;
        final String checkedUrl = getUrlForHttp(urlString);
        try {
             HttpHead httpHead = new HttpHead(checkedUrl);
             response = httpclient.execute(httpHead, new ResponseHandler<HttpResponse>() {
                @Override
                public HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode==405){
                        response = httpclient.execute(new HttpGet(checkedUrl));
                    }
                    if (statusCode==404||statusCode==403||!response.containsHeader("Content-Length")){
                        response = getHttpResponseByProxy(checkedUrl);
                    }
                    return response;
                }
            });

        }
        catch (Exception e) {
           response = getHttpResponseByProxy(checkedUrl);
        }
        return response;
    }
    private static HttpResponse getHttpResponseByProxy(String urlString){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpHead httpHead = new HttpHead(PROXY_URL+urlString);
        HttpResponse response=null;
        try {
             response = httpclient.execute(httpHead);
        }
        catch (IOException ignored) {
        }
        return response;
    }
    private static URL getUrlForFtp(String urlString) throws MalformedURLException {
        urlString = urlString.replaceAll("%20"," ");
        urlString = urlString.replaceAll("%13","\n");
        urlString = urlString.replaceAll("%26","&");
        urlString = urlString.replaceAll("%5B","[");
        urlString = urlString.replaceAll("%5D","]");
        urlString = urlString.replaceAll("%2B","+");
        return new URL(urlString);
    }
    static String getUrlForHttp(String urlString)  {
        urlString = urlString.replaceAll("\\{","%7B");
        urlString = urlString.replaceAll("\\}","%7D");
        urlString = urlString.replaceAll("\\[","%5B");
        urlString = urlString.replaceAll("\\]","%5D");
        urlString=urlString.replaceAll("\\\\","/");
        return urlString;
    }


    // TODO: 15.04.2016. Check ResponseHandler for notOpen Manuals(404 answer). Decide where to check is Manual size legit or not.
}
