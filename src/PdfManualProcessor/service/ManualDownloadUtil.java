package PdfManualProcessor.service;

import PdfManualProcessor.Manual;
import PdfManualProcessor.multithreading.ManualSizeGetter;
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
import java.util.concurrent.*;

/**
 * This class implements all downloading logic.
 */
public class ManualDownloadUtil {
    private static final String PROXY_URL = "http://74.117.180.69:83/work/pdfapprove/get_pdf_curl.php?url=";
    private static final String DOWNLOAD_DIR = "D:\\pdf.Storage\\";  //change it to directory from properties.

    /**
     * Gets manuals' size.
     * Selects which download method (http or ftp) to use, according to manuals' url.
     * @param m - Manual to download.
     */
    public static void download(Manual m){
        //Getting manuals estimated size.
        m.setSize(getManualSize(m));

        //Selecting which method to use.
        if (m.getPdfUrl().toLowerCase().startsWith("ftp")){
            downloadFtpManual(m);
        }
        else downloadHttpManual(m);
    }

    /**
     * Downloads manual, located on ftp server.
     * @param m - Manual to download.
     */
    private static void downloadFtpManual(Manual m){
        System.out.println("ftp skipped. "+m.getPdfUrl());
    }

    /**
     * Downloads manual, located on http/https server.
     * @param m - Manual to download.
     */
    private static void downloadHttpManual(Manual m){
        //Checking size, if it is not legit - trying to get it again.
        if (m.getSize()<1024) m.setSize(ManualSizeChecker.getManualSize(m.getPdfUrl()));

        //Preparing file, at which we are planning to save manual. (Name is manuals' ID - its always unique.)
        File file = new File(DOWNLOAD_DIR+m.getId()+".pdf");

        //Creating httpclient.
        CloseableHttpClient httpclient = HttpClients.createDefault();
       try {
           //Getting response with manual body. Saving it to file.
           savePdfFile(getResponse(m,httpclient),file);
           httpclient.close();
       }
       catch (IOException ignored){}

        //Checking manual file - if it is corrupted, starting download by proxy.
        if (isCorrupt(m)){
            System.out.println("starting download via proxy manual - "+m.getId());
            downloadManualByProxy(m);
        }
    }

    /**
     * Downloading manual via proxy. Also we use downloading logic different from
     * previous download method. Sometimes httpClient manages exceptions wrong - and
     * after such cases (not only after such) we call this method.
     * @param m - Manual to download.
     */
    private static void downloadManualByProxy(Manual m){
        //Preparing file, to which we will save manuals' body.
        File file = new File(DOWNLOAD_DIR+m.getId()+".pdf");

        try {
            //Getting proper URL (changing incorrect symbols to correct)
            URL url = new URL(PROXY_URL+m.getPdfUrl());

            //Opening channel.
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());

            //Opening output stream.
            FileOutputStream fos = new FileOutputStream(file);

            //Transferring manuals' body from web to file.
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            //Closing resources.
            fos.close();
            rbc.close();
        }
        catch (IOException ignored){}
    }

    /**
     * Gets httpResponse, which entity should be manuals' body.
     * @param m - Manual to download.
     * @param httpclient - HttpClient, which executes get request.
     * @return httpResponse, which entity should be manuals' body.
     * @throws IOException
     */
    private static HttpResponse getResponse(Manual m, HttpClient httpclient) throws IOException {
        //Building httpGet request.
        HttpGet httpget = null;
        try {
            //will throw exception, if manuals' url contains incorrect symbols.
            httpget =  new HttpGet(m.getPdfUrl());
        }
        catch (IllegalArgumentException e){
            //Changing incorrect symbols in manuals' url to correct.
            httpget = new HttpGet(ManualSizeChecker.getCorrectUrlForHttp(m.getPdfUrl()));
        }
        //Preparing HttpResponse object.
        HttpResponse result = null;

        //Executing httpGet request.
        try{
            result = httpclient.execute(httpget);
        }
        catch (SSLException e){
            //In 80% cases SSLException can be solved, by changing https to http in manuals' url. So we do it.
            if (m.getPdfUrl().toLowerCase().startsWith("https")){
                String s = m.getPdfUrl();
                s = s.replace("https", "http");
                m.setPdfUrl(s);
                result = getResponse(m,httpclient);
            }
        }
        return result;
    }

    /**
     * Saves manual body to file.
     * @param response - HttpResponse, which entity should be manuals' body.
     * @param file - file, where to save manuals' body.
     * @throws IOException
     */
    private static void savePdfFile(HttpResponse response, File file) throws IOException {
        //Preparing entity object.
        HttpEntity entity = null;

        //Checking if response is null.
        if (response != null) {
            entity = response.getEntity();
        }

        //If entity is not null - saving manuals' body to file.
        if (entity != null) {
            InputStream inputStream=entity.getContent();
            Files.copy(inputStream, file.toPath());
            inputStream.close();
        }
    }

    /**
     * Checks if manual was downloaded correctly, or not.
     * @param m - downloaded manual.
     * @return True, if manuals' file is corrupted.
     */
    private static boolean isCorrupt(Manual m){
        //Getting downloaded manuals' file.
        File file = new File(DOWNLOAD_DIR+m.getId()+".pdf");

        //Getting downloaded manuals' file size.
        long fileSize = file.length();

        //if size is less that 1024 bytes - file is surely corrupted.
        if (fileSize<1024)return true;

        //if size is higher that 8kb and it equals to estimated size - we assume that file is correct.
        //This assumption is correct in 99,9% of cases (estimation from experience).
        // As next check method is slow - we choose to neglect possible file corruption,
        // rather than to check each file by trying to read it from disk.
        if (fileSize==m.getSize()&&fileSize>8192)return false;

        //For all cases left, we checking manuals consistency by reading its first page
        //from disk. PdfReader will send exception if manuals' file is corrupted.
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

    /**
     * Gets manualSize
     * @param m - manual, which estimated size we are trying to get.
     * @return estimated size for Manual m.
     */
    private static Integer getManualSize(Manual m){
        Integer result = 0;

        //Starting ExecutorService.
        ExecutorService es = Executors.newSingleThreadExecutor();

        //Starting task, which should return manuals' estimated size or 0, in case if any exception occurs.
        Future<Integer> future = es.submit(new ManualSizeGetter(m.getPdfUrl()));
        try {
            //If method works longer that 3 seconds, we abort it and return 0.
            result=future.get(3,TimeUnit.SECONDS);
        } catch (Exception e) {
            result=0;
        }
        es.shutdown();
        return result;
    }

    public static void main(String[] args) {
        download(new Manual("1111249","https://archive.icann.org/en/tlds/org/applications/register/attachments/hardware/servers/COLOR-IBM-x330.pdf" ,0));
       // System.out.println(isCorrupt(new Manual("4139409","http://www.autometer.com/media/manual/2650-1727.pdf" ,0)));
    }

    //todo:  Implement downloadFTP method. Decide minimum legit size for manual. Delete main method.
    //todo:  Manage fileAlreadyExists exception, after downloading restart.
    //todo:  Rework reading part from isCorrupt method. Such logic should be implemented in ManualReader class.
    //todo:  Check file creation.
    //todo:  Check in savePdfFile() method - was pdf file saved or not.

}
