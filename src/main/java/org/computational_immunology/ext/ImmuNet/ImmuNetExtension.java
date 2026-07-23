package org.computational_immunology.ext.ImmuNet;

import org.computational_immunology.ext.ImmuNet.core.handlers.ImageRequestHandler;
import org.computational_immunology.ext.ImmuNet.core.handlers.ServerConnectionHandler;
import org.computational_immunology.ext.ImmuNet.ui.DatasetSelectorTab;
import org.computational_immunology.ext.ImmuNet.ui.ServerConnectionTab;
import org.computational_immunology.ext.ImmuNet.ui.MainViewerListener;

import qupath.lib.gui.QuPathGUI;
import qupath.lib.gui.extensions.QuPathExtension;
import qupath.lib.gui.viewer.QuPathViewer;

public class ImmuNetExtension implements QuPathExtension {

    @Override
    public void installExtension(QuPathGUI qupath) {
        qupath.getMenu("ImmuNet", true); // Add new tab to top menu bar

        // Built once here and injected down, this will be used to retrieve specifically tile images from the server
        ImageRequestHandler imageRequestHandler = new ImageRequestHandler(ServerConnectionHandler.getInstance());

        // Side bar
        ServerConnectionTab serverConnectionTab = new ServerConnectionTab();
        serverConnectionTab.addCustomTab(qupath.getAnalysisTabPane());

        DatasetSelectorTab datasetTab = new DatasetSelectorTab(imageRequestHandler);
        datasetTab.addCustomTab(qupath.getAnalysisTabPane());

        //viewer listener
        QuPathViewer viewer = qupath.getViewer();
        if (viewer != null) {
            viewer.addViewerListener(new MainViewerListener());
        }
    }

    @Override
    public String getName() {
        return "ImmuNet Extension";
    }

    @Override
    public String getDescription() {
        return "Extension adding connection to Immunology Department.";
    }
}
