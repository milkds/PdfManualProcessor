package PdfManualProcessor.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TextAreaFromConsoleSample {
    static String filePath = "file:////D:/test.pdf";
    static JTextArea textArea = new JTextArea();

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame ("Test");
        frame.setSize(500,500);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("working");
            }
        });
        JPanel panel = new JPanel();
        panel.add(okButton);



        textArea.setSize(400,300);

        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setVisible(true);

        JScrollPane scroll = new JScrollPane (textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scroll);
        frame.add(panel, BorderLayout.WEST);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        redirectSystemStreams();

    }


    private static void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textArea.append(" "+text); //с отступом.
            }
        });
    }
    private static void redirectSystemStreams() {
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

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    public static void init(){
        JFrame frame = new JFrame ("Test");
        frame.setSize(500,500);
        JButton okButton = new JButton("OK");

        JPanel panel = new JPanel();
        panel.add(okButton);



        textArea.setSize(400,300);

        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setVisible(true);

        JScrollPane scroll = new JScrollPane (textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scroll);
        frame.add(panel, BorderLayout.WEST);
        // frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        redirectSystemStreams();
        someMethodForTest();
    }

    private static void someMethodForTest(){

        for (int i = 0; i <10 ; i++) {
            System.out.println(" step no "+i);
        }
    }
}
