package PdfManualProcessor.view;

import PdfManualProcessor.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        buttons = new JPanel();
        process = new JPanel();
        down= new JPanel();
        up = new JPanel();

        JLabel label = new JLabel(new ImageIcon("C:\\Users\\AUSU\\IdeaProjects\\ideaGitLearning\\src\\PdfManualProcessor\\res\\img.png"));
        up.add(label);

        process.setLayout(new BorderLayout());
        down.setLayout(new GridLayout(1,2,5,5));
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));

        scroll = new JScrollPane(console);

        cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewHandler.fireEventCancelLongAction();
            }
        });

        progressBar = new LongActionProgressBar();


        buttons.add(cancel,BorderLayout.NORTH);
        process.add(scroll,BorderLayout.CENTER);
        process.add(progressBar,BorderLayout.NORTH);

        down.add(process);
        down.add(buttons,BorderLayout.EAST);

        getContentPane().add(up,BorderLayout.NORTH);
        getContentPane().add(down,BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LongActionView(new Console(),new ViewHandler(new Controller()));
    }
}
