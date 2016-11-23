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
import java.awt.event.*;

/**
 * This class shows Manuals for visual check by User. Also launches manual deletion in system.
 */
public class ManualCheckView extends JFrame implements View {

    private JButton moveManual, delete;
    private JPanel left,right,buttons,lists;
    private JList manualList;
    private JScrollPane scroll;
    private JComboBox strategyList;
    private Container contentPane;
    private SwingController controller;

    private Strategy strategy;

    /**
     * Strategy is initialised in constructor, to avoid NPE, while building all view.
     * @throws HeadlessException
     */
    public ManualCheckView() throws HeadlessException {
        this.strategy = new AllDeleteStrategy();
        init();
    }

    @Override
    public void init() {
        //Initialising panels.
        left = new JPanel(new BorderLayout(5,5));
        buttons= new JPanel();
        lists = new JPanel(new BorderLayout(0,5));

        //Initialising buttons.
        moveManual = new JButton("move manual");
        moveManual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Logically removing manual.
                strategy.onRemove((String) manualList.getSelectedValue());

                //Checking selection index, to avoid exception when index is 0 and we
                //try to select -1.
                int index = manualList.getSelectedIndex();
                if (index>0)manualList.setSelectedIndex(index+1);
                else manualList.setSelectedIndex(0);

                //Removing manual from view.
                ((DefaultListModel) manualList.getModel()).remove(index);
                manualList.revalidate();
                manualList.grabFocus(); //This needed to make keyListeners active again.
            }
        });
        delete = new JButton("delete manuals");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strategy.onRemoveAll();
            }
        });

        //Initialising manual list.
        initScroll();

        //Adding box for User to choose what type of visual check he is going to do.
        String[] strategies = {"checkDelete", "sureDelete", "allDelete"};
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

        //Initialising manual preview panel.
        right= initManualViewPanel("D:\\pdf.Storage\\"+manualList.getSelectedValue()+".pdf");

        //Filling panels.
        buttons.add(moveManual);
        buttons.add(delete);

        lists.add(strategyList, BorderLayout.NORTH);
        lists.add(scroll, BorderLayout.CENTER);

        left.add(buttons,BorderLayout.NORTH);
        left.add(lists,BorderLayout.CENTER);

        //Filling main panel.
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(left,BorderLayout.WEST);
        contentPane.add(right,BorderLayout.EAST);

        //Initialising view.
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Initialises manual list.
     */
    private void initScroll(){
        //Getting list of Manual's IDs.
        String[] manuals = strategy.getManualList();

        //Filling model.
        DefaultListModel<String> model = new DefaultListModel();
        for (String m: manuals){
            model.addElement(m);
        }

        //Initialising list view.
        manualList = new JList(model);

        //Making list scrollable.
        scroll = new JScrollPane(manualList);
    }

    /**
     * Initialising Manual's pdf files preview.
     * @param filePath - location on disk of file being showed.
     * @return Panel with Manual's file preview.
     */
    private JPanel initManualViewPanel(String filePath){
        JPanel result;

        //Manual previewing lib's magic.
        controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        result = factory.buildViewerPanel();
        ComponentKeyBinding.install(controller, result);
        controller.setPageViewMode( DocumentViewControllerImpl.ONE_PAGE_VIEW, false);
        manualList.grabFocus();

        return result;
    }

    /**
     * Updates manual body view with new manual.
     */
    private void updateView(){
        controller.closeDocument();
        right.removeAll();

        //Removing existing panel.
        contentPane.remove(right);
        right = null;
        //Generating new panel.
        right= initManualViewPanel("D:\\pdf.Storage\\"+manualList.getSelectedValue()+".pdf");
        controller.openDocument("D:\\pdf.Storage\\"+manualList.getSelectedValue()+".pdf");
        manualList.grabFocus();

        //Adding new panel to main panel.
        contentPane.add(right,BorderLayout.EAST);

        //Drawing new panel.
        revalidate();
        repaint();
    }

    /**
     * Updates manual list view.
     */
    private void updateScroll(){
        //Removing old list.
        lists.remove(scroll);

        //Creating new one.
        initScroll();

        //Setting selection index to 0.
        manualList.setSelectedIndex(0);

        //Adding selectionListener.
        manualList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateView();
            }
        });

        //Adding listener to remove manuals from list by pressing Space button.
        manualList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key==KeyEvent.VK_SPACE){
                    moveManual.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //do nothing
            }
        });

        //Adding list to panel.
        lists.add(scroll, BorderLayout.CENTER);

        //Drawing list.
        revalidate();
    }
}
