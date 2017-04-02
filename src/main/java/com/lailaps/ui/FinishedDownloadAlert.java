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

public class FinishedDownloadAlert extends Alert {

    private static final Logger LOG = Logger.getLogger(FinishedDownloadAlert.class);
    private static final String HEADER = "lailaps finished downloading";
    private final Insets GRID_PADDING = new Insets(20, 150, 10, 10);
    private final int GRID_HGAP = 50;
    private final int GRID_VGAP = 10;

    private ButtonType buttonTypeShowFolder = new ButtonType("Show Folder");
    private ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
    private DownloadStatistics statistics;

    public FinishedDownloadAlert(DownloadStatistics statistics) {
        super(AlertType.INFORMATION);
        this.statistics = statistics;
    }

    public void initContent() {
        setTitle(HEADER);
        setHeaderText(null);
        getDialogPane().setContent(createContent());
        setButtons();
    }

    private Node createContent() {
        GridPane grid = initGrid();
        List<Pair> relevantStatistics = statistics.getDisplayableStats();
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
        getButtonTypes().setAll(buttonTypeShowFolder, buttonTypeOK);
    }

    public void displayAndWait() {
        showAndWait()
                .filter(response -> response == buttonTypeShowFolder)
                .ifPresent(response -> openFolder());

    }

    private void openFolder() {
        try {
            File folder = new File(statistics.getDownloadFolderLocation());
            Desktop.getDesktop().open(folder);
        } catch (IOException e) {
            LOG.info(e);
        }
    }
}
