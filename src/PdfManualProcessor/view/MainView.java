package PdfManualProcessor.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame implements View {
    private JButton download, filter, refresh,next, prev;
    private JScrollPane scroll;
    private JPanel box,openers;
    private JTextField nextQuantity, prevQuantity;
    private JLabel nManuals, pManuals;
    private Console console;
    private ViewHandler viewHandler;


    public MainView(Console console, ViewHandler viewHandler) {
        super();
        this.console=console;
        this.viewHandler = viewHandler;
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        viewHandler.fireEventRefreshManualList();
                    }
                }).start();

            }
        });
        initOpeners(); // different abstraction level - rework.
        box = new JPanel(new GridLayout(4, 1,0,5));

        box.add(refresh);
        box.add(download);
        box.add(filter);
        box.add(openers);

        JPanel west = new JPanel(new GridBagLayout());
        west.add(box);

        scroll = new JScrollPane(console);

        getContentPane().add(west, BorderLayout.WEST);
        getContentPane().add(scroll);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initOpeners(){
        openers = new JPanel(new GridLayout(2, 3,5,5));
        next = new JButton("Open next");
        prev = new JButton("Open previous");
        nextQuantity = new JTextField();
        prevQuantity = new JTextField();
        nManuals = new JLabel("manuals");
        pManuals = new JLabel("manuals");

        openers.add(next);
        openers.add(nextQuantity);
        openers.add(nManuals);
        openers.add(prev);
        openers.add(prevQuantity);
        openers.add(pManuals);
    }


    public static void main(String[] args) {
        Console console = new Console();
        new MainView(console, null);
    }
}
