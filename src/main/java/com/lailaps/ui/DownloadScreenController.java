package com.lailaps.ui;

import com.lailaps.download.DownloadObserver;
import com.lailaps.download.DownloadableDocument;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class DownloadScreenController implements Initializable, Controllable, AutoResizable, DownloadObserver {

    private static final Logger LOG = Logger.getLogger(DownloadScreenController.class);
    private ScreenContainer parent;
    private ObservableList<ProgressLabelBox> boxes = FXCollections.observableArrayList();

    private double TEST = 0.0;

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
            box.bindProgressBarWidthProperty(listView);
        }
    }

    public void determineTimeToBindSizes() {
        pane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                bindComponentsToStageSize();
            }
        });
    }

    @FXML
    public void testAddDownload() {
        DownloadableDocument doc = new DownloadableDocument("test", "", "", ".pdf");
        startDownload(doc);
        onDownloadProgress(doc, 0);
    }

    @Override
    public void startDownload(DownloadableDocument document) {
        LOG.info("start download.");

        ProgressLabelBox box = new ProgressLabelBox(100, document.getName());
        boxes.add(box);
        box.bindProgressBarWidthProperty(listView);
    }

    @Override
    public void onDownloadProgress(DownloadableDocument document, double currentProgress) {
        ProgressLabelBox box = boxes.get(0);
        box.setProgress(TEST);
        TEST += 0.1;
    }

    @Override
    public void skippedDownload(DownloadableDocument skippedDocument, boolean isError) {
        LOG.info("skipped Download");
    }

    @Override
    public void finishedDownloading() {
        LOG.info("finished.");
    }
}
