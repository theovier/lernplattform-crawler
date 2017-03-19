package com.lailaps.ui;


import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

public class ProgressLabelBox extends HBox {

    private static final double PARENT_WIDTH_RATIO = 0.3;
    private ProgressBar progressBar = new ProgressBar(0);
    private Label label = new Label();
    private Insets labelPadding = new Insets(0,0,0,30);

    public ProgressLabelBox(double progress) {
        super();
        setProgress(progress);
        setText("random long file name is standing here");
        initialize();
    }

    public ProgressLabelBox(double progress, String text) {
        super();
        setProgress(progress);
        setText(text);
        initialize();
    }

    private void initialize() {
        getChildren().addAll(progressBar, label);
        label.setPadding(labelPadding);
        progressBar.setPrefWidth(300);
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
