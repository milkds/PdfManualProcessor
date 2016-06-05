package PdfManualProcessor.view;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;

import javax.swing.*;
import java.awt.*;

public class ViewerComponentExample {
    public static void main(String[] args) {
        String filePath = "D:\\test.pdf";
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
       // controller.setIsEmbeddedComponent(true);


        DocumentViewController viewController = controller.getDocumentViewController();

        JPanel viewerComponentPanel = factory.buildViewerPanel();
        ComponentKeyBinding.install(controller, viewerComponentPanel);
      //  viewController.setAnnotationCallback( new org.icepdf.ri.common.MyAnnotationCallback( viewController));

        JFrame applicationFrame = new JFrame();
        applicationFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        applicationFrame.getContentPane().setLayout(new BorderLayout());
        applicationFrame.getContentPane().add(viewerComponentPanel, BorderLayout.CENTER);
        applicationFrame.getContentPane().add(factory.buildCompleteMenuBar(), BorderLayout.NORTH);
        controller.setPageViewMode( DocumentViewControllerImpl.ONE_PAGE_VIEW, false);

        controller.openDocument(filePath);

        applicationFrame.pack();
        applicationFrame.setVisible(true);
    }
}
