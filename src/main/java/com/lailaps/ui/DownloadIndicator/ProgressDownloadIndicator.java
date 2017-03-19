package com.lailaps.ui.DownloadIndicator;


import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class ProgressDownloadIndicator extends HBox{

    private static final double PARENT_WIDTH_RATIO = 0.3;
    protected ProgressBar progressBar = new ProgressBar(0);
    protected Label label = new Label();
    private Insets labelPadding = new Insets(0,0,0,30);

    public ProgressDownloadIndicator(double progress) {
        super();
        setProgress(progress);
        setText("random long file name is standing here");
        initialize();
    }

    public ProgressDownloadIndicator(double progress, String text) {
        super();
        setProgress(progress);
        setText(text);
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
}
