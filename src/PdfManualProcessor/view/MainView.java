package PdfManualProcessor.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame implements View {
    private JButton download, filter, refresh;
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
            }
        });
        filter = new JButton("Filter manuals");
        refresh = new JButton("Refresh manual list");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewHandler.fireEventRefreshManualList();
            }
        });

        box = new JPanel(new GridLayout(3, 1,0,5));
        box.add(refresh);
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
