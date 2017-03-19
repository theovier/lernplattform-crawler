package com.lailaps.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DownloadScreenController implements Initializable, Controllable, AutoResizable {

    private ScreenContainer parent;
    private ObservableList<ProgressLabelBox> boxes = FXCollections.observableArrayList();

    @FXML
    private BorderPane pane;

    @FXML
    private MenuBar menubar;

    @FXML
    private ListView<ProgressLabelBox> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        determineTimeToBindSizes();

        for (int i = 0; i <= 100; i++) {
            ProgressLabelBox box = new ProgressLabelBox(0.01f * i);
            boxes.add(box);
        }
        listView.setItems(boxes);

    }

    @Override
    public void setParentScreen(ScreenContainer parent) {
        this.parent = parent;
    }

    @Override
    public ScreenContainer getParentScreen() {
        return parent;
    }

    @Override
    public void bindComponentsToStageSize() {
        Stage stage = (Stage) pane.getScene().getWindow();
        pane.prefWidthProperty().bind(stage.widthProperty());
        menubar.prefWidthProperty().bind(stage.widthProperty());
        listView.prefWidthProperty().bind(stage.widthProperty());
        pane.prefHeightProperty().bind(stage.heightProperty().subtract(50));

        for (ProgressLabelBox box : boxes) {
            box.bindProgressBarWidthProperty(listView.widthProperty().multiply(0.3));
        }


    }

    public void determineTimeToBindSizes() {
        pane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                bindComponentsToStageSize();
            }
        });
    }
}
