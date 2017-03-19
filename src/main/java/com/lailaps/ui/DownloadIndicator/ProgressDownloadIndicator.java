package com.lailaps.ui.DownloadIndicator;


import com.lailaps.download.DownloadableDocument;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class ProgressDownloadIndicator extends HBox {

    private static final double PARENT_WIDTH_RATIO = 0.3;
    protected ProgressBar progressBar = new ProgressBar(0);
    protected Label label = new Label();
    private Insets labelPadding = new Insets(0,0,0,30);
    private DownloadableDocument correspondingDocument;

    public ProgressDownloadIndicator(DownloadableDocument document) {
        super();
        correspondingDocument = document;
        setText(correspondingDocument.toString());
        initialize();
    }

    //make protected when not in testing anymore.
    public ProgressDownloadIndicator(DownloadableDocument document, double progress) {
        super();
        setProgress(progress);
        setText(document.toString());
        correspondingDocument = document;
        initialize();
    }

    private void initialize() {
        getChildren().addAll(progressBar, label);
        label.setPadding(labelPadding);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void bindProgressBarWidthProperty(Control parent) {
        progressBar.prefWidthProperty().bind(parent.widthProperty().multiply(PARENT_WIDTH_RATIO));
    }

    public boolean isReferringToSameDocument(DownloadableDocument document) {
        return correspondingDocument.equals(document);
    }
}
