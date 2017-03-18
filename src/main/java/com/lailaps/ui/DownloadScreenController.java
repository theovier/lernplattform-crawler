package com.lailaps.ui;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DownloadScreenController implements Initializable, Controllable {

    private ScreenManager parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void setParentScreen(ScreenManager parent) {
        this.parent = parent;
    }

    @Override
    public ScreenManager getParentScreen() {
        return parent;
    }
}
