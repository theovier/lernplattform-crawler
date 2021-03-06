package com.lailaps.ui;

import com.lailaps.download.DownloadObserver;
import com.lailaps.download.DownloadStatistics;
import com.lailaps.download.DownloadableDocument;
import com.lailaps.ui.DownloadIndicator.FailedDownloadIndicator;
import com.lailaps.ui.DownloadIndicator.ProgressDownloadIndicator;
import com.lailaps.ui.DownloadIndicator.SkippedDownloadIndicator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;


public class DownloadScreenController implements Initializable, Controllable, AutoResizable, DownloadObserver {

    private ScreenContainer parent;
    private ObservableList<ProgressDownloadIndicator> downloadIndicators = FXCollections.observableArrayList();
    private ResourceBundle bundle;

    @FXML
    private BorderPane pane;

    @FXML
    private Label headline;

    @FXML
    private ProgressIndicator throbber;

    @FXML
    private ListView<ProgressDownloadIndicator> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        headline.setText(bundle.getString("downloadScreen.headline"));
        determineTimeToBindSizes();
        listView.setItems(downloadIndicators);
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
        listView.prefWidthProperty().bind(stage.widthProperty());
        pane.prefHeightProperty().bind(stage.heightProperty().subtract(50));

        for (ProgressDownloadIndicator line : downloadIndicators) {
            line.bindProgressBarWidthProperty(listView);
        }
    }

    @Override
    public void determineTimeToBindSizes() {
        pane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                bindComponentsToStageSize();
            }
        });
    }

    private boolean updateDownloadIndicatorForDocument(DownloadableDocument document, double currentProgress) {
        for (ProgressDownloadIndicator indicator : downloadIndicators) {
            if (indicator.isReferringToSameDocument(document)) {
                indicator.setProgress(currentProgress);
                return true;
            }
        }
        return false;
    }

    private boolean removeDownloadIndicatorForDocument(DownloadableDocument document) {
        for (ProgressDownloadIndicator indicator : downloadIndicators) {
            if (indicator.isReferringToSameDocument(document)) {
                downloadIndicators.remove(indicator);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDownloadStarted(DownloadableDocument document) {
        ProgressDownloadIndicator newIndicator = new ProgressDownloadIndicator(document);
        downloadIndicators.add(newIndicator);
        newIndicator.bindProgressBarWidthProperty(listView);
    }

    @Override
    public void onDownloadProgress(DownloadableDocument document, double currentProgress) {
        updateDownloadIndicatorForDocument(document, currentProgress);
    }

    @Override
    public void onDownloadSkipped(DownloadableDocument skippedDocument) {
        SkippedDownloadIndicator skippingIndicator = new SkippedDownloadIndicator(skippedDocument, bundle);
        skippingIndicator.bindProgressBarWidthProperty(listView);
        downloadIndicators.add(skippingIndicator);
    }

    @Override
    public void onDownloadFailed(DownloadableDocument failedDocument, Exception cause) {
        removeDownloadIndicatorForDocument(failedDocument);
        FailedDownloadIndicator failingIndicator = new FailedDownloadIndicator(failedDocument, cause);
        failingIndicator.bindProgressBarWidthProperty(listView);
        downloadIndicators.add(failingIndicator);
    }

    @Override
    public void onDownloadSuccess(DownloadableDocument downloadedDocument) {
        //empty
    }

    @Override
    public void onFinishedDownloading(DownloadStatistics statistics) {
        throbber.setVisible(false);
        headline.setText(bundle.getString("downloadScreen.finished"));
        FinishedDownloadAlert alert = new FinishedDownloadAlert(statistics, bundle);
        alert.initContent();
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(parent.getCurrentWindow());
        alert.displayAndWait();
    }
}
