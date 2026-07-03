package org.computational_immunology;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class DatasetSelectorTab extends CustomSidePanelTab {

    public DatasetSelectorTab() {
        super("Image selector");
    }

    /**
     * Creates a panel with a button and two boxes underneath eachother.
     * @return The full panel with button and box components.
     */
    @Override
    public VBox getContent(){
        VBox sidePanelTab = new VBox();
        sidePanelTab.setPadding(new Insets(10, 10, 10, 10)); // Box margins
        sidePanelTab.setSpacing(5); // Space between buttons and boxes

        // Interactive selection boxes
        ListViewerBox dsBox = new ListViewerBox(300, sidePanelTab.getMaxWidth()); // Dataset
        ListViewerBox tsBox = new ListViewerBox(300, sidePanelTab.getMaxWidth()); // Tissue slide

        Button loadDataBtn = makeButton("Load Datasets", new Dimensions(40, 100)); 
        loadDataBtn.setOnAction(e -> MenuActions.updateListViewerBox(dsBox, getDatasets()));

        Button openImgBtn = makeButton("Open Image", new Dimensions(40, 100)); 
        openImgBtn.setOnAction(e -> {
            try{
                String dsName = dsBox.getListView().getSelectionModel().selectedItemProperty().getValue();
                String tsName = tsBox.getListView().getSelectionModel().selectedItemProperty().getValue();
                MenuActions.setStreamedServer(dsName, tsName);
            } catch (NullPointerException exc){
                ImmuNetLog.error("No dataset of slide selected for opening.", exc);
            }
        });

        updateSlideByDataset(dsBox, tsBox);
        
        sidePanelTab.getChildren().addAll(loadDataBtn, dsBox.getBox(), tsBox.getBox(), openImgBtn);

        return sidePanelTab;
    }

    private List<String> getDatasets(){
        return ServerRequestHandler.getWebpageAsList("datasets/");
    }

    private List<String> getSlides(String dataset){
        return ServerRequestHandler.getWebpageAsList("datasets/" + dataset + "/");
    }

    private void updateSlideByDataset(ListViewerBox datasetBox, ListViewerBox slideBox){
        datasetBox.getListView().getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null){
                    MenuActions.updateListViewerBox(slideBox, getSlides(newValue)); // Update tissue slide box
                    ImmuNetLog.log("Selected: " + newValue);
                }
            }
        );
    }
}
