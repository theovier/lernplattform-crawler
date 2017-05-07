package com.lailaps.ui;

import com.lailaps.download.DownloadStatistics;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.geometry.Insets;
import org.apache.log4j.Logger;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class FinishedDownloadAlert extends Alert {

    private static final Logger LOG = Logger.getLogger(FinishedDownloadAlert.class);
    private final Insets GRID_PADDING = new Insets(20, 150, 10, 10);
    private final int GRID_HGAP = 50;
    private final int GRID_VGAP = 10;

    private ButtonType buttonTypeShowFolder;
    private ButtonType buttonTypeOK;
    private final DownloadStatistics STATISTICS;
    private final ResourceBundle BUNDLE;

    public FinishedDownloadAlert(final DownloadStatistics statistics, final ResourceBundle bundle) {
        super(AlertType.INFORMATION);
        this.STATISTICS = statistics;
        this.BUNDLE = bundle;
    }

    public void initContent() {
        buttonTypeShowFolder = new ButtonType(BUNDLE.getString("downloadScreen.showFolder"));
        buttonTypeOK = new ButtonType(BUNDLE.getString("misc.ok"), ButtonBar.ButtonData.CANCEL_CLOSE);
        setTitle(BUNDLE.getString("download.statistics.header"));
        setHeaderText(null);
        getDialogPane().setContent(createContent());
        setButtons();
    }

    private Node createContent() {
        GridPane grid = initGrid();
        List<Pair> relevantStatistics = STATISTICS.getDisplayableStats();
        for (int i = 0; i < relevantStatistics.size(); i++) {
            Pair stat = relevantStatistics.get(i);
            Label description = new Label("" + stat.getKey());
            Label amount = new Label("" + stat.getValue());
            grid.add(description, 0, i);
            grid.add(amount, 1, i);
        }
        return grid;
    }

    private GridPane initGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(GRID_HGAP);
        grid.setVgap(GRID_VGAP);
        grid.setPadding(GRID_PADDING);
        return grid;
    }

    private void setButtons() {
        if (Desktop.isDesktopSupported()) {
            //we are able to browse to the folder, so show the button.
            getButtonTypes().setAll(buttonTypeShowFolder, buttonTypeOK);
        } else {
            getButtonTypes().setAll(buttonTypeOK);
        }
    }

    public void displayAndWait() {
        showAndWait()
                .filter(response -> response == buttonTypeShowFolder)
                .ifPresent(response -> openFolder());

    }

    private void openFolder() {
        File folder = new File(STATISTICS.getDownloadFolderLocation());
        new Thread(() -> {
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException e) {
                LOG.error(e);
            }
        }).start();
    }
}
