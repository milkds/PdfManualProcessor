package PdfManualProcessor.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame implements View {

    public MainView() {
        super();
    }

    @Override
    public void init() {


        setSize(500,500);
        setPreferredSize(new Dimension(500,500));
        Container pane = getContentPane();

        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        GroupLayout.Group horButtonGroup = groupLayout.createParallelGroup();
        GroupLayout.Group verButtonGroup = groupLayout.createSequentialGroup();
        groupLayout.setHorizontalGroup(horButtonGroup);
        groupLayout.setVerticalGroup(verButtonGroup);

        JButton button = new JButton("Download manuals");
        JButton button2 = new JButton("Filter manuals");

        horButtonGroup.addComponent(button);
        horButtonGroup.addComponent(button2);
        verButtonGroup.addComponent(button);
        verButtonGroup.addComponent(button2);

        pack();


       setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       setVisible(true);

    }

    public static void main(String[] args) {
        new MainView().init();
    }
}
