package PdfManualProcessor.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is Panel which is always shown on program launch.
 */

public class MainView extends JFrame implements View {
    private Console console;
    private ViewHandler viewHandler;

    private JButton download, filter, refresh, check,next, prev;
    private JScrollPane scroll;
    private JPanel box,openers,west;
    private JLabel nManuals, pManuals;
    private SpinnerModel nextModel,prevModel;
    private JSpinner nextSpinner,prevSpinner;

    public MainView(Console console, ViewHandler viewHandler) {
        super();
        this.console=console;
        this.viewHandler = viewHandler;
        init();
    }

    @Override
    public void init() {
        // Initialising panels.
        box = new JPanel(new GridLayout(5, 1,0,5));
        west = new JPanel(new GridBagLayout());
        scroll = new JScrollPane(console);

        //Initialising buttons.
        initButtons();

        //Initialising panel for manual in browser opening.
        initOpeners();

        //Filling box panel.
        box.add(refresh);
        box.add(download);
        box.add(filter);
        box.add(check);
        box.add(openers);

        //Placing panels.
        west.add(box);
        getContentPane().add(west, BorderLayout.WEST);
        getContentPane().add(scroll);

        //Initialising view.
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Initialises panel, used for manual opening in browser.
     */
    private void initOpeners(){
        //Initialising panel
        openers = new JPanel(new GridLayout(2,3,5,5));

        //Initialising buttons.
        next = new JButton("Open next"); //Used in normal mode for opening number of manuals chosen by User.
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //will be opening manuals from List<Manual>, so we reduce quantity to open by 1
                viewHandler.fireEventOpenNextManualsInBrowser((int)nextModel.getValue()-1);
            }
        });
        prev = new JButton("Open previous"); //Used if any mistake occurred after opening and its necessary to reopen.
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHandler.fireEventOpenPrevManualsInBrowser((int)prevModel.getValue()-1);
            }
        });

        //Initialising labels.
        nManuals = new JLabel("manuals");
        pManuals = new JLabel("manuals");

        //Initialising spinners. (This is for user to choose number of manuals to open/reopen. Also it prevents user to enter incorrect value.
        nextModel = new SpinnerNumberModel(1,0,null,1);
        prevModel = new SpinnerNumberModel(1,0,null,1);
        nextSpinner = new JSpinner(nextModel);
        prevSpinner = new JSpinner(prevModel);

        //Filling panel (order is important, as we have Grid layout).
        openers.add(next);
        openers.add(nextSpinner);
        openers.add(nManuals);
        openers.add(prev);
        openers.add(prevSpinner);
        openers.add(pManuals);
    }

    /**
     * Initialises buttons.
     */
    private void initButtons(){
        download = new JButton("Download manuals"); //launches manual downloading.
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHandler.fireEventDownloadManuals();
            }
        });
        filter = new JButton("Filter manuals"); //launches manual filtration.
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHandler.fireEventFilterManuals();
            }
        });
        refresh = new JButton("Refresh manual list"); //launches manual list update.
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
        check = new JButton("Check manuals"); // Opens interface for User's visual check.
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHandler.fireEventCheckManuals();
            }
        });
    }

    public static void main(String[] args) {
        Console console = new Console();
        new MainView(console, null);
    }
}

//todo: name box panel correctly.
//todo: remove Main() method.
