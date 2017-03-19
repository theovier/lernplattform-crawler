package com.lailaps.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DownloadScreenController implements Initializable, Controllable, AutoResizable {

    private ScreenContainer parent;

    @FXML
    private BorderPane pane;

    @FXML
    private MenuBar menubar;

    @FXML
    private ListView<ProgressBar> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        determineTimeToBindSizes();
        ObservableList<ProgressBar> bars = FXCollections.observableArrayList();

        for (int i = 0; i <= 100; i++) {
            ProgressBar a = new ProgressBar(0.01f * i);
            a.setPrefWidth(300);
            bars.add(a);
        }
        listView.setItems(bars);
        for (ProgressBar bar : bars) {
           // bar.setProgress(1);
        }
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
    }

    public void determineTimeToBindSizes() {
        pane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                bindComponentsToStageSize();
            }
        });
    }
}
