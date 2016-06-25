package PdfManualProcessor.view;

import PdfManualProcessor.view.strategy.AllDeleteStrategy;
import PdfManualProcessor.view.strategy.CheckDeleteStrategy;
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

public class ManualCheckView extends JFrame implements View {

    private JButton moveManual, delete;
    private JPanel left,right,buttons,lists;
    private JList manualList;
    private JScrollPane scroll;
    private JComboBox strategyList;
    private Container contentPane;

    private Strategy strategy;

    public ManualCheckView() throws HeadlessException {
        this.strategy = new AllDeleteStrategy();
        init();
    }

    @Override
    public void init() {
        left = new JPanel(new BorderLayout(5,5));
        buttons= new JPanel();
        lists = new JPanel(new BorderLayout(0,5));
        moveManual = new JButton("move manual");
        moveManual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strategy.onRemove((String) manualList.getSelectedValue());
                int index = manualList.getSelectedIndex();
                if (index>0)manualList.setSelectedIndex(index-1);
                else manualList.setSelectedIndex(0);

                ((DefaultListModel) manualList.getModel()).remove(index);

                manualList.revalidate();
            }
        });
        delete = new JButton("delete manuals");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strategy.onRemoveAll();
            }
        });

        buttons.add(moveManual);
        buttons.add(delete);
        initScroll();
        lists.add(scroll, BorderLayout.CENTER);

        String [] strategies = {"checkDelete", "sureDelete", "allDelete"};
        strategyList = new JComboBox(strategies);
        strategyList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = strategyList.getSelectedIndex();
                switch (index){
                    case 0: strategy = new CheckDeleteStrategy(); break;
                    case 1: strategy = new SureDeleteStrategy(); break;
                    case 2: strategy = new AllDeleteStrategy(); break;
                }
                updateScroll();
            }
        });
        strategyList.setSelectedIndex(0);

        right= initManualViewPanel("D:\\pdf.Storage\\"+manualList.getSelectedValue()+".pdf");

        lists.add(strategyList, BorderLayout.NORTH);
        lists.add(scroll, BorderLayout.CENTER);

        left.add(buttons,BorderLayout.NORTH);
        left.add(lists,BorderLayout.CENTER);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(left,BorderLayout.WEST);
        contentPane.add(right,BorderLayout.EAST);


        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ManualCheckView();
    }

    private void initScroll(){
        String[] manuals = strategy.getManualList();
        DefaultListModel<String> model = new DefaultListModel();
        for (String m: manuals){
            model.addElement(m);
        }
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
        if (manualList.getMaxSelectionIndex()>0) {
            controller.openDocument(filePath);
        }

        return result;
    }

    private void updateView(){
        contentPane.remove(right);

        right= initManualViewPanel("D:\\pdf.Storage\\"+manualList.getSelectedValue()+".pdf");
        contentPane.add(right,BorderLayout.EAST);
        revalidate();
        repaint();
    }

    private void updateScroll(){
        lists.remove(scroll);
        initScroll();
        manualList.setSelectedIndex(0);
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
                //do nothing
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //do nothing
            }
        });
        lists.add(scroll, BorderLayout.CENTER);

        revalidate();
    }
}
