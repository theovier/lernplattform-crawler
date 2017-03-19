package com.lailaps.ui;

import com.lailaps.download.DownloadObserver;
import com.lailaps.download.DownloadableDocument;
import com.lailaps.ui.DownloadIndicator.FailedDownloadIndicator;
import com.lailaps.ui.DownloadIndicator.ProgressDownloadIndicator;
import com.lailaps.ui.DownloadIndicator.SkippedDownloadIndicator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DownloadScreenController implements Initializable, Controllable, AutoResizable, DownloadObserver {

    private ScreenContainer parent;
    private ObservableList<ProgressDownloadIndicator> lines = FXCollections.observableArrayList();

    @FXML
    private BorderPane pane;

    @FXML
    private MenuBar menubar;

    @FXML
    private ListView<ProgressDownloadIndicator> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        determineTimeToBindSizes();

        for (int i = 0; i <= 10; i++) {
            ProgressDownloadIndicator box = new ProgressDownloadIndicator(0.1f * i, "test");
            lines.add(box);
        }
        listView.setItems(lines);
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

        for (ProgressDownloadIndicator line : lines) {
            line.bindProgressBarWidthProperty(listView);
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
        onDownloadStarted(doc);
        onDownloadSkipped(doc);
        onDownloadFailed(doc, new IOException("IO Exception"));
        onDownloadProgress(doc, 0);
    }

    @Override
    public void onDownloadStarted(DownloadableDocument document) {
        //add it to some kind of hashMap/list

        ProgressDownloadIndicator line = new ProgressDownloadIndicator(100, document);
        lines.add(line);
        line.bindProgressBarWidthProperty(listView);
    }

    @Override
    public void onDownloadProgress(DownloadableDocument document, double currentProgress) {
        //there has to be a line to this document already. get it.
    }

    @Override
    public void onDownloadSkipped(DownloadableDocument skippedDocument) {
        ProgressDownloadIndicator line = new SkippedDownloadIndicator("skipped");
        line.bindProgressBarWidthProperty(listView);
        lines.add(line);
    }

    @Override
    public void onDownloadFailed(DownloadableDocument failedDocument, Exception cause) {

    }

    @Override
    public void onDownloadFinished() {
        //display some text
    }
}
