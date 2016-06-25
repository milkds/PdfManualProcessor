package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

public class ManualDownloadUtil {
    private static final String PROXY_URL = "http://74.117.180.69:83/work/pdfapprove/get_pdf_curl.php?url=";
    private static final String DOWNLOAD_DIR = "D:\\pdf.Storage\\";  //change it to directory from properties

    public static void download(Manual m){
        m.setSize(ManualSizeChecker.getManualSize(m.getPdfUrl()));
        if (m.getPdfUrl().toLowerCase().startsWith("ftp")){
            downloadHttpManual(m);
        }
       // else downloadHttpManual(m);
    }

    private static void downloadFtpManual(Manual m){
        System.out.println("ftp skipped. "+m.getPdfUrl());
    }
    private static void downloadHttpManual(Manual m){
        if (m.getSize()<1024) m.setSize(ManualSizeChecker.getManualSize(m.getPdfUrl()));
        File file = new File(DOWNLOAD_DIR+m.getId()+".pdf");
        //do it here to make HttpClient close correctly. If we do it in getResponse method we will be not able, to get entity of it
        CloseableHttpClient httpclient = HttpClients.createDefault();
       try {
           savePdfFile(getResponse(m,httpclient),file);
           httpclient.close();
       }
       catch (IOException ignored){
           ignored.printStackTrace();
       }
        long fileSize = file.length();
        int size = m.getSize();
        System.out.println(m.getPdfUrl()+" size is "+fileSize +". Estimated size "+size);
        if (isCorrupt(m)){
            System.out.println("starting download via proxy manual - "+m.getId());
              downloadManualByProxy(m);
        }

    }

    private static void downloadManualByProxy(Manual m){
        //we don't use HttpClient here, to get manual in alternative way.
        File file = new File(DOWNLOAD_DIR+m.getId()+".pdf");
        try {
            URL url = new URL(PROXY_URL+m.getPdfUrl());
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        }
        catch (IOException ignored){
            ignored.printStackTrace();
        }
    }

    private static HttpResponse getResponse(Manual m, HttpClient httpclient) throws IOException {
        HttpGet httpget = null;
        try {
            httpget =  new HttpGet(m.getPdfUrl());
        }
        catch (IllegalArgumentException e){
            httpget = new HttpGet(ManualSizeChecker.getUrlForHttp(m.getPdfUrl()));
        }
        HttpResponse response = null;
        try{
            response = httpclient.execute(httpget);
        }
        catch (SSLException e){
            if (m.getPdfUrl().contains("https")){
                String s = m.getPdfUrl();
                s = s.replaceAll("https", "http");
                m.setPdfUrl(s);
                response = getResponse(m,httpclient);
            }
        }
        return response;
    }
    private static void savePdfFile(HttpResponse response, File file) throws IOException {
        HttpEntity entity = null;
        if (response != null) {
            entity = response.getEntity();
        }
        if (entity != null) {
            InputStream inputStream=entity.getContent();
            Files.copy(inputStream, file.toPath());
            inputStream.close();
        }
    }

    private static boolean isCorrupt(Manual m){
        File file = new File(DOWNLOAD_DIR+m.getId()+".pdf");
        long fileSize = file.length();
        if (fileSize<1024)return true;
        if (fileSize==m.getSize()&&fileSize>8192)return false;
        try {
            PdfReader pdfReader = new PdfReader(file.getAbsolutePath());
            PdfTextExtractor.getTextFromPage( pdfReader, 1 );
            pdfReader.close();
        }
        catch (Exception e) {
            return true;
        }
        return false;
    }



    public static void main(String[] args) {
        download(new Manual("1111249","https://archive.icann.org/en/tlds/org/applications/register/attachments/hardware/servers/COLOR-IBM-x330.pdf" ,0));
       // System.out.println(isCorrupt(new Manual("4139409","http://www.autometer.com/media/manual/2650-1727.pdf" ,0)));
    }

}
