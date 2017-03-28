package com.lailaps.ui;

import com.lailaps.download.DownloadStatistics;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class FinishedDownloadAlert extends Alert {

    private static final Logger LOG = Logger.getLogger(FinishedDownloadAlert.class);
    private static final String HEADER = "lailaps finished downloading";
    private ButtonType buttonTypeShowFolder = new ButtonType("Show Folder");
    private ButtonType buttonTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    private DownloadStatistics statistics;

    public FinishedDownloadAlert(DownloadStatistics statistics) {
        super(AlertType.INFORMATION);
        this.statistics = statistics;
    }

    public void initContent() {
        setTitle(HEADER);
        setHeaderText(null);
        setContentText(createContentText());
        setButtons();
    }

    private String createContentText() {
        return String.format(
                "new documents downloaded: %1$d" + System.lineSeparator() +
                "documents already existed (skipped): %2$d" + System.lineSeparator() +
                "failed to download documents: %3$d",
                statistics.getDownloadCount(), statistics.getSkippedCount(), statistics.getFailedCount());
    }

    private void setButtons() {
        getButtonTypes().setAll(buttonTypeShowFolder, buttonTypeOK);
    }

    public void displayAndWait() {
        Optional<ButtonType> result = showAndWait();
        if (result.get() == buttonTypeShowFolder) {
            openFolder();
        }
    }

    private void openFolder() {
        try {
            String folderLocation = statistics.getDownloadFolderLocation();
            LOG.info(folderLocation);

            //todo on mac not explorer.exe
            Runtime.getRuntime().exec("explorer.exe /select," + Paths.get(folderLocation));
        } catch (IOException e) {
            LOG.info(e);
        }
    }
}
