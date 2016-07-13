package PdfManualProcessor.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class gets estimated size for manual pdf file.
 */
public class ManualSizeChecker {
    private static final String PROXY_URL = "http://74.117.180.69:83/work/pdfapprove/get_pdf_curl.php?url=";

    /**
     * Returns manual file estimated size. Decides which checking method
     * to call, according to manual location (ftp or http(s)).
     * @param urlString - Manuals url.
     * @return Manual estimated size.
     */
    public static int getManualSize(String urlString){
        if (urlString.toLowerCase().startsWith("ftp"))return getSizeByFtp(urlString);
        else return getSizeByHttp(urlString);
    }

    /**
     * Gets estimated size for manuals, located on FTP servers.
     * @param urlString - Manuals url.
     * @return estimated size for manual.
     */
    private static int getSizeByFtp(String urlString){
        int result = 0;
        URL url = null;

        //Initializing FTPClient
        FTPClient ftpClient = new FTPClient();

        try {
            //Getting correct URL.
            url = getUrlForFtp(urlString);

            //Connecting to FTP server with default login/password.
            ftpClient.connect(url.getHost());
            ftpClient.login("anonymous","user@domain.net");

            //Switching to binary mode.
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //Sending SIZE request.
            ftpClient.sendCommand("SIZE", url.getPath());

            //If reply is correct, getting size from it.
            if(ftpClient.getReplyCode()==213) {
                String reply = ftpClient.getReplyString();
                result = Integer.parseInt(reply.substring(reply.lastIndexOf(" ")).trim());
            }
        }
        catch (IOException ignored) {
        }
        return result;
    }

    /**
     * Gets estimated size for manuals, located on HTTP(s) servers.
     * @param urlString - Manual URL.
     * @return estimated size for manual.
     */
    private static int getSizeByHttp(String urlString){
        return getSizeByHttp(getHttpResponse(urlString));
    }

    /**
     * Parses estimated size for manuals, located on HTTP(s) servers from response for http request.
     * @param response - response for http request.
     * @return estimated size for manual.
     */
    private static int getSizeByHttp(HttpResponse response){
        int result =0;

        //Checking if response is null (to avoid NPE).
        if (response==null)return result;

        //Trying to get header, which should contain manuals estimated size.
        Header header = response.getFirstHeader("Content-Length");

        //If header with size is present, getting its value.
        if(header!=null){
            result = Integer.parseInt(header.getValue());
        }

        return result;
    }

    /**
     * Prepares and sends http request to get size in answer.
     * @param urlString - Manuals URL.
     * @return Response for http request, which should contain manuals estimated size.
     */
    private static HttpResponse getHttpResponse(String urlString){
        //Initialising http client and preparing variable for return.
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response;

        //Changing incorrect symbols in URL to correct (according to HttpClient logic).
        final String checkedUrl = getUrlForHttp(urlString);

        try {
            //Preparing http Head request.
             HttpHead httpHead = new HttpHead(checkedUrl);

            //Executing request, getting response.
             response = httpclient.execute(httpHead, new ResponseHandler<HttpResponse>() {

                @Override
                //Implementing response handler, as recommended by HttpClient documentation.
                public HttpResponse handleResponse(HttpResponse response) throws IOException {
                    //Getting response status code.
                    int statusCode = response.getStatusLine().getStatusCode();

                    //If server doesn't accept HEAD request - sending GET request.
                    if (statusCode==405){
                        response = httpclient.execute(new HttpGet(checkedUrl));
                    }

                    //If server returns 403/404 answer, or has no header with manual size - trying to get request via proxy.
                    if (statusCode==404||statusCode==403||!response.containsHeader("Content-Length")){
                        response = getHttpResponseByProxy(checkedUrl);
                    }
                    return response;
                }
            });
        }
        catch (Exception e) {
            //If any exception occurs - retrying request via proxy.
           response = getHttpResponseByProxy(checkedUrl);
        }
        return response;
    }

    /**
     * Prepares and sends http request to get size in answer via proxy.
     * @param urlString Manuals url.
     * @return Response for http request, which should contain manuals estimated size.
     */
    private static HttpResponse getHttpResponseByProxy(String urlString){
        //Initialising Http Client.
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //Preparing Http Head request.
        HttpHead httpHead = new HttpHead(PROXY_URL+urlString);

        //Trying to get response.
        HttpResponse response=null;
        try {
             response = httpclient.execute(httpHead);
        }
        catch (IOException ignored) {
        }
        return response;
    }

    /**
     * Changes incorrect symbols in URL to correct for FTP Client.
     * @param urlString Manual's URL with incorrect symbols.
     * @return Manual's URL with correct symbols.
     * @throws MalformedURLException
     */
    private static URL getUrlForFtp(String urlString) throws MalformedURLException {
        urlString = urlString.replaceAll("%20"," ");
        urlString = urlString.replaceAll("%13","\n");
        urlString = urlString.replaceAll("%26","&");
        urlString = urlString.replaceAll("%5B","[");
        urlString = urlString.replaceAll("%5D","]");
        urlString = urlString.replaceAll("%2B","+");
        return new URL(urlString);
    }

    /**
     * hanges incorrect symbols in URL to correct for HTTP Client.
     * @param urlString Manual's URL with incorrect symbols.
     * @return Manual's URL with correct symbols.
     */
    static String getUrlForHttp(String urlString)  {
        urlString = urlString.replaceAll("\\{","%7B");
        urlString = urlString.replaceAll("\\}","%7D");
        urlString = urlString.replaceAll("\\[","%5B");
        urlString = urlString.replaceAll("\\]","%5D");
        urlString=urlString.replaceAll("\\\\","/");
        return urlString;
    }

    // TODO: 15.04.2016. Check ResponseHandler for notOpen Manuals(404 answer). Decide where to check if Manual's size legit or not.
}
