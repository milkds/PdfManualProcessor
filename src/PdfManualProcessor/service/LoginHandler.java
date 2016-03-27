package PdfManualProcessor.service;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Login class. Should also contain some constants connected with login request compilation.
 */
public class LoginHandler {
    private static final String HOST = "74.117.180.69";
    private static final int PORT = 83;
    private static final String LOGIN_REQUEST_FORMAT ="POST http://"+HOST+":"+PORT+"/work/pdfapprove/index.php?action=login HTTP/1.1\n" +
            "Host: "+HOST+":"+PORT+"\n" +
            "Content-Length: %d\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "Connection: close\n\n" +
            "auth=1&login=%s&password=%s";
    private static final String GET_HTML_PAGE_REQUEST_FORMAT = "GET http://"+HOST+":"+PORT+"/work/pdfapprove/index.php?page=%d HTTP/1.1\n" +
            "Host: "+HOST+":"+PORT+"\n" +
            "Cookie:login=%s; password=%s; %s\n\n";

    public static String getHtmlPage(int pageNo,String login, String password) throws IOException {
        String request = String.format(GET_HTML_PAGE_REQUEST_FORMAT,pageNo,login,password,getCookie(login,password));
        Socket socket = getSocket();
        sendRequest(socket,request);
        return getServerAnswer(socket);
    }
    public static String getCookie(String login, String password) throws SocketException {
        Socket socket = getSocket();
        String answer;
        String cookie ="";
        try {
            sendRequest(socket,getLoginRequest(login,password));
            answer=getServerAnswer(socket);
            cookie=answer.substring(answer.indexOf("SESSID"),answer.indexOf(';'));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }
    /**
     * @return complete Http login request
     */
    public static String getLoginRequest(String login, String password){
        int contentLength = 23+login.length()+password.length(); //23 is the constant length of auth line.
        return String.format(LOGIN_REQUEST_FORMAT,contentLength,login,password);
    }
    public static Socket getSocket() throws SocketException {
        int count = 0;
        while (true) {
            try {
                return new Socket(HOST, PORT);
            } catch (IOException e) {
                count++;
                if (count == 3) throw new SocketException();
            }
        }
    }
    public static void sendRequest(Socket socket, String request) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(request.getBytes());
    }
    public static String getServerAnswer(Socket socket) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int ch = bfr.read();
        while (ch != -1) {
            sb.append((char) ch);
            ch = bfr.read();
        }
        socket.close();
        return sb.toString();
    }

    /**
     * Main method to be deleted after class is ready.
     */
    public static void main(String[] args) throws IOException {
        System.out.println(getHtmlPage(15,"xxx","xxx"));
    }
    // TODO: check for possible Exceptions, remake JavaDoc, remake getting Html page for use with password hash in request instead of bare password.
}
