package com.lailaps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainFX extends Application {

    public static void main(String[] args) {
        Application.launch(MainFX.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL loginScreen = getClass().getClassLoader().getResource("fxml/login.fxml");
        Parent root = FXMLLoader.load(loginScreen);
        stage.setScene(new Scene(root));
        stage.show();
    }


}