package PdfManualProcessor.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This view is shown, while long work (downloading manuals, reading manuals, etc.) is in process.
 */

public class LongActionView extends JFrame implements View {

    private Console console;
    private ViewHandler viewHandler;

    private JPanel buttons, process,down,up;
    private JButton cancel;
    private JScrollPane scroll;
    private LongActionProgressBar progressBar;


    public LongActionView(Console console, ViewHandler viewHandler) throws HeadlessException {
        this.console = console;
        this.viewHandler = viewHandler;
        init();
    }

    public void setProgressBar(LongActionProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public LongActionProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void init() {
        //Initialising panels.
        buttons = new JPanel();
        process = new JPanel();
        down= new JPanel();
        up = new JPanel();

        //Setting layouts
        process.setLayout(new BorderLayout());
        down.setLayout(new GridLayout(1,2,5,5));
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Setting picture to show while downloading (decorative function).
        JLabel label = new JLabel(new ImageIcon("C:\\Users\\AUSU\\IdeaProjects\\ideaGitLearning\\src\\PdfManualProcessor\\res\\img.png"));
        up.add(label);

        //Making console scrollable.
        scroll = new JScrollPane(console);

        //Initialising buttons.
        cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHandler.fireEventCancelLongAction();
            }
        });

        //Initialising progress bar
        progressBar = new LongActionProgressBar();

        //Filling panels.
        buttons.add(cancel,BorderLayout.NORTH);

        process.add(scroll,BorderLayout.CENTER);
        process.add(progressBar,BorderLayout.NORTH);

        down.add(process);
        down.add(buttons,BorderLayout.EAST);

        //Placing panels.
        getContentPane().add(up,BorderLayout.NORTH);
        getContentPane().add(down,BorderLayout.CENTER);

        //Initialising view
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}