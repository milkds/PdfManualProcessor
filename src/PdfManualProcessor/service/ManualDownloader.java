package PdfManualProcessor.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class downloads Manuals to Disk.
 *
 * Main idea of class is next:
 *
 * Ask for file size (add new field to manual class)
 * try to download Manual from proxy.
 * check downloaded file size. If it is not equal to file size - try to get it by direct link.
 *
 * If file size is not reachable - check file consistency via PDFReader.
 *
 * we have 3 types of urls - ftp, http and https
 *
 * http and https we do the same - parse host from urlString. Send head request to it - get Content-Length field.
 *
 * For ftp we use algorithm from sampleFTPMethod
 *
 *
 *
 */
public class ManualDownloader {
    //

    public static void main(String[] args) throws IOException {
        headHttpRequestSampleMethod();
    }


    public static void sampleFtpMethod() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("study-films.ru");
        ftpClient.login("anonymous","user@domain.net");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //  ftpClient.setPassiveNatWorkaround(true);
        // code to connect and login....
        String filePath = "/Public/MOBILE PHONES/1 Top Brand/Samsung/Samsung GT-E2370/GT-E2370 Hardware/GT-E2370_sm/E2370Disassembly & Reassembly.pdf";

        ftpClient.sendCommand("SIZE", filePath);
        String reply = ftpClient.getReplyString();
        System.out.println("Reply for SIZE command: " + reply);

        //ftp://mail.racs.co.bw/Canon/MultiPass%20Smartbase/MPC%20SERIES/MPC200/Manuals/SmartBase_MPC200%20Basic%20Guide.pdf
        //ftp://study-films.ru/Public/MOBILE%20PHONES/1%20Top%20Brand/Samsung/Samsung%20GT-E2370/GT-E2370%20Hardware/GT-E2370_sm/E2370Disassembly%20%26%20Reassembly.pdf
        //ftp://support.corvalent.com/P65AX-P65MX/Docs/MN-P65MX-02.pdf
        /*логин: anonymous
        пароль: user@domain.net*/
    }

    /***
     * Next two methods are not actual. Doing same by HttpClient from Apache.
     * @throws IOException
     */
    public static void headHttpRequestSampleMethod() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HEAD http://www.sylvane.com/media/documents/products/soleus-ky5-110-ac-manual.pdf HTTP/1.0\n");
        sb.append("Host: www.sylvane.com \n\n");
        Socket socket = new Socket("www.sylvane.com",80);
        OutputStream out = socket.getOutputStream();
          /*  URL url = new URL("http://diagramasde.com/diagramas/audio/AV-D37%20-%202.pdf");*/
       /* URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();*/

        out.write(sb.toString().getBytes());

        String result = null;
        StringBuilder sbf = new StringBuilder();
        try {
            InputStream is = socket.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            int ch = bfr.read();
            while (ch != -1)
            { sbf.append((char) ch);
                ch = bfr.read(); }
            result=sbf.toString();
            is.close();

        }
        catch (IOException e)
        {
            System.out.println("no answer");
        }
        System.out.println(result);
    }

    public static void tryUrlParseWithRegexpMethod(){
        String ip =
                "http://([^/]*?)/";
        String s = "http://www.heronhill.co.uk/downloads/pdf/pdf352.pdf";
        Pattern pattern = Pattern.compile(ip);
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
