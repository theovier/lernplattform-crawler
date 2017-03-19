package com.lailaps;

import com.lailaps.ui.ScreenManager;
import com.lailaps.ui.ScreenType;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class MainFX extends Application {

    public static String title = "lailaps";

    public static void main(String[] args) {
        Application.launch(MainFX.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ScreenManager screenManager = new ScreenManager();
        screenManager.loadAllScreens();
        screenManager.showScreen(ScreenType.LOGIN);

        Group root = new Group();
        root.getChildren().addAll(screenManager);
        stage.setScene(new Scene(root));
        stage.show();

        Image icon = new Image(getClass().getClassLoader().getResourceAsStream("images/lailaps2_32x32.png"));
        stage.getIcons().add(icon);
        stage.setTitle(title);
    }
}