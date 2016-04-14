package PdfManualProcessor.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

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
    public static int getSizeByHttp(String url){
        int result =0;
        return result;
    }
    public static URL getUrlForFtp(String urlString) throws MalformedURLException {
        urlString = urlString.replaceAll("%20"," ");
        urlString = urlString.replaceAll("%13","\n");
        urlString = urlString.replaceAll("%26","&");
        urlString = urlString.replaceAll("%5B","[");
        urlString = urlString.replaceAll("%5D","]");
        urlString = urlString.replaceAll("%2B","+");
        return new URL(urlString);
    }
}
