package PdfManualProcessor.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame implements View {
    private JButton download, filter;
    private JScrollPane scroll;
    private JPanel box;
    private Console console;

    public MainView(Console console) {
        super();
        this.console=console;
        init();
    }

    @Override
    public void init() {
        download = new JButton("Download manuals");
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("click click");
            }
        });
        filter = new JButton("Filter manuals");

        box = new JPanel(new GridLayout(2, 1));
        box.add(download);
        box.add(filter);
        JPanel west = new JPanel(new GridBagLayout());
        west.add(box);

        scroll = new JScrollPane(console);

        getContentPane().add(west, BorderLayout.WEST);
        getContentPane().add(scroll);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


    public static void main(String[] args) {
        Console console = new Console();
        new MainView(console);
    }
}
