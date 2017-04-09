package com.lailaps.ui;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;



public class AboutAlert extends Alert {

    private static final String HEADER = "About lailaps";

    public AboutAlert() {
        super(AlertType.NONE);
        initStyle(StageStyle.UTILITY);
        setTitle(HEADER);
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        initModality(Modality.NONE);
        setContentText("created by: Theo Harkenbusch" +  System.lineSeparator() + "go to github: www.google.de");
        close();
    }
}
