package com.lailaps.ui;


import javafx.application.HostServices;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.image.Image ;

import java.util.ArrayList;
import java.util.List;


public class AboutAlert extends Alert {

    private static final String HEADER = "About lailaps";
    private static final String HYPERLINK_TEXT = "http://www.github.com";
    private static final String HYPERLINK_LINK = HYPERLINK_TEXT;
    private final HostServices hostServices;

    public AboutAlert(final HostServices hostServices) {
        super(AlertType.NONE);
        this.hostServices = hostServices;
        setTitle(HEADER);
        initStyle(StageStyle.UTILITY);
        initModality(Modality.NONE);
        setAndHideButton();
        getDialogPane().setContent(createContent());
    }

    //we need a button otherwise the alert can't be closed
    private void setAndHideButton() {
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
    }

    private Node createContent() {
        GridPane gridPane = new GridPane();
        List<Node> lines = getLines();
        for (int i = 0; i < lines.size(); i++) {
            Node line = lines.get(i);
            GridPane.setHalignment(line, HPos.CENTER);
            gridPane.add(line, 0, i);
        }
        return gridPane;
    }

    private List<Node> getLines() {
        List<Node> lines = new ArrayList<>();
        lines.add(getLogoLine());
        lines.add(getVersionNumberLine());
        lines.add(getCreatorLine());
        lines.add(new Label(""));
        lines.add(getHyperlinkLine());
        return lines;
    }

    private Node getLogoLine() {
        Image icon = new Image(getClass().getClassLoader().getResourceAsStream("images/lailaps_32x32.png"));
        return new ImageView(icon);
    }

    private Node getVersionNumberLine() {
        String version = "v1.4.1";
        return new Label(version);
    }

    private Node getCreatorLine() {
        return new Label("created by Theo Harkenbusch");
    }

    private Node getHyperlinkLine() {
        FlowPane hyperlinkContainer = new FlowPane();
        Hyperlink hyperlink = new Hyperlink(HYPERLINK_TEXT);
        hyperlink.setOnAction(e -> hostServices.showDocument(HYPERLINK_LINK));
        Label description = new Label("get the sourcecode at ");
        hyperlinkContainer.getChildren().addAll(description, hyperlink);
        hyperlinkContainer.setAlignment(Pos.CENTER);
        return hyperlinkContainer;
    }
}
