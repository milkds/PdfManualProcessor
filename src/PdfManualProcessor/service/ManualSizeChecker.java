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

    public static int getSizeByFtp(String urlString){
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
               /* System.out.println(urlString);*/
                String reply = ftpClient.getReplyString();
                result = Integer.parseInt(reply.substring(reply.lastIndexOf(" ")).trim());
            }
        }
        catch (IOException ignored) {
        }
        return result;
    }
    public static int getSizeByHttp(String urlString){
        return getSizeByHttp(getHttpResponse(urlString));
    }

    private static int getSizeByHttp(HttpResponse response){
        int result =0;
        Header header = response.getFirstHeader("Content-Length");
        if(header!=null){
            result = Integer.parseInt(header.getValue());
        }
        return result;
    }
    private static HttpResponse getHttpResponse(String urlString){
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
        catch (IOException e) {
           response = getHttpResponseByProxy(checkedUrl);
        }
        return response;
    }
    private static HttpResponse getHttpResponseByProxy(String urlString){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpHead httpHead = new HttpHead(urlString);
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
    private static String getUrlForHttp(String urlString)  {
        urlString = urlString.replaceAll("\\{","%7B");
        urlString = urlString.replaceAll("\\}","%7D");
        urlString=urlString.replaceAll("\\\\","/");
        return urlString;
    }


    // TODO: 15.04.2016. Check ResponseHandler for notOpen Manuals(404 answer). Decide where to check is Manual size legit or not.
}
