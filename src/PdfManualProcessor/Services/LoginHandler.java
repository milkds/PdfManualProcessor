package PdfManualProcessor.Services;

/**
 * Login class. Should also contain some constants connected with login request compilation.
 */
public class LoginHandler {
    private static final String HOST = "74.117.180.69";
    private static final String PORT = "83";
    private static final String LOGIN_REQUEST_FORMAT ="POST http://74.117.180.69:83/work/pdfapprove/index.php?action=login HTTP/1.0\n" +
            "Host: "+HOST+":"+PORT+"\n" +
            "Content-Length: %d\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "Connection: close\n\n" +
            "auth=1&login=%s&password=%s";

    public static String getCookie(String login, String password){

        return null;
    }

    /**
     * @return complete Http login request
     */
    public static String getLoginRequest(String login, String password){
        int contentLength = 23+login.length()+password.length(); //23 is the constant length of auth line.
        return String.format(LOGIN_REQUEST_FORMAT,contentLength,login,password);
    }

    public static void main(String[] args) {
        System.out.println(getLoginRequest("xxx","xxx")); //for testing - to be deleted
    }
    // TODO: implement methods, remake JavaDoc
}
