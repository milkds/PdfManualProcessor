package PdfManualProcessor.view;

import PdfManualProcessor.Manual;
import PdfManualProcessor.service.ManualSerializer;
import PdfManualProcessor.view.strategy.Strategy;
import PdfManualProcessor.view.strategy.SureDeleteStrategy;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class ManualCheckView extends JFrame implements View {

    private JButton moveManual;
    private JPanel left,right,buttons;
    private JList manualList;
    private JScrollPane scroll;
    private Container contentPane;

    private Strategy strategy;

    public ManualCheckView(Strategy strategy) throws HeadlessException {
        this.strategy = strategy;
        init();
    }

    @Override
    public void init() {
        left = new JPanel(new BorderLayout(5,5));
        initManualViewPanel("D:\\test.pdf");
        initScroll();
        buttons= new JPanel();
        moveManual = new JButton("move manual");
        moveManual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = manualList.getSelectedIndex();
                if (index>0)manualList.setSelectedIndex(index-1);
                else manualList.setSelectedIndex(0);
                ((DefaultListModel) manualList.getModel()).remove(index);

                manualList.revalidate();
            }
        });


        buttons.add(moveManual);
        manualList.setSelectedIndex(0);
        right= initManualViewPanel("D:\\test\\"+manualList.getSelectedValue()+".pdf");
        manualList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
               updateView();
            }
        });
        manualList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int key = e.getKeyCode();
                if (key==KeyEvent.VK_UP){
                    System.out.println(manualList.getSelectedIndex());
                    manualList.setSelectedIndex(manualList.getSelectedIndex()-1);
                }
                if (key==KeyEvent.VK_DOWN){manualList.setSelectedIndex(manualList.getSelectedIndex()+1);
                    System.out.println(manualList.getSelectedIndex());}
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        left.add(buttons,BorderLayout.NORTH);
        left.add(scroll,BorderLayout.CENTER);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(left,BorderLayout.WEST);
        contentPane.add(right,BorderLayout.EAST);


        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ManualCheckView(new SureDeleteStrategy()).init();
    }

    private void initScroll(){
        List<Manual> manuals = ManualSerializer.getManualsForReopening(); //for tests. List will be changed later on
        DefaultListModel<String> model = new DefaultListModel();
        for (Manual m: manuals){
            model.addElement(m.getId());
        }

        /*String[]manualList2 = new String[manuals.size()];
        for (int i = 0; i <manuals.size() ; i++) {
            manualList2[i]=manuals.get(i).getId();
        }*/
        manualList = new JList(model);
        scroll = new JScrollPane(manualList);
    }

    private JPanel initManualViewPanel(String filePath){
        JPanel result;
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        result = factory.buildViewerPanel();
        ComponentKeyBinding.install(controller, result);
        controller.setPageViewMode( DocumentViewControllerImpl.ONE_PAGE_VIEW, false);

        controller.openDocument(filePath);

        return result;
    }

    private void updateView(){
        contentPane.remove(right);
        right= initManualViewPanel("D:\\test\\"+manualList.getSelectedValue()+".pdf");
        contentPane.add(right,BorderLayout.EAST);
        revalidate();
        repaint();
    }
}
