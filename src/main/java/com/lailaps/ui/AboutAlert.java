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
import java.util.ResourceBundle;


public class AboutAlert extends Alert {

    private final HostServices HOSTSERVICES;
    private final ResourceBundle BUNDLE;

    public AboutAlert(final HostServices hostServices, final ResourceBundle bundle) {
        super(AlertType.NONE);
        this.HOSTSERVICES = hostServices;
        this.BUNDLE = bundle;
        setTitle(BUNDLE.getString("menu.about"));
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
        String version = "v"+ getClass().getPackage().getImplementationVersion();
        return new Label(version);
    }

    private Node getCreatorLine() {
        return new Label(BUNDLE.getString("menu.about.author"));
    }

    private Node getHyperlinkLine() {
        FlowPane hyperlinkContainer = new FlowPane();
        Hyperlink hyperlink = getHyperlink();
        Label description = new Label(BUNDLE.getString("menu.about.getSource"));
        hyperlinkContainer.getChildren().addAll(description, hyperlink);
        hyperlinkContainer.setAlignment(Pos.CENTER);
        return hyperlinkContainer;
    }

    private Hyperlink getHyperlink() {
        Hyperlink hyperlink = new Hyperlink(BUNDLE.getString("menu.about.source"));
        hyperlink.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        hyperlink.setOnAction(e -> HOSTSERVICES.showDocument(hyperlink.getText()));
        return hyperlink;
    }
}
