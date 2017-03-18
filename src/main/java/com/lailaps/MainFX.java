package com.lailaps;

import com.lailaps.ui.ScreenManager;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainFX extends Application {

    public static final String LOGIN_SCREEN = "login";
    public static final String LOGIN_SCREEN_FILE = "fxml/login.fxml";
    public static final String DOWNLOAD_SCREEN = "download";
    public static final String DOWNLOAD_SCREEN_FILE = "fxml/download.fxml";


    public static void main(String[] args) {
        Application.launch(MainFX.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ScreenManager screenManager = new ScreenManager();
        screenManager.loadScreen(LOGIN_SCREEN, LOGIN_SCREEN_FILE);
        screenManager.loadScreen(DOWNLOAD_SCREEN, DOWNLOAD_SCREEN_FILE);
        screenManager.showScreen(LOGIN_SCREEN);
        screenManager.showScreen(DOWNLOAD_SCREEN);

        Group root = new Group();
        root.getChildren().addAll(screenManager);
        stage.setScene(new Scene(root));
        stage.show();
    }
}