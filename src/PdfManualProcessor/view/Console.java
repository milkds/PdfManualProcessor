package PdfManualProcessor.view;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This class is made for redirecting console output to JTextArea, same for each window.
 */
public class Console extends JTextArea {

    public Console() {
        super();
        init();
    }

    private void init() {
        //implementing some additional settings
        setEditable(false);
        setLineWrap(true);

        //Launching main logic.
        redirectSystemStreams();
    }

    /**
     * Redirects all console output to JTextArea.
     */
    private void redirectSystemStreams() {
        //Implementing our version of OutputStream()
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        //Changing System constants.
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    /**
     * Updates JTextArea.
     * @param text - text to show in console.
     */
    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               append(" "+text); //space for better message view
            }
        });
    }
}
