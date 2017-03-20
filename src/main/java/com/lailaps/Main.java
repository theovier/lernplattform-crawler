package com.lailaps;

import com.lailaps.ui.ScreenContainer;
import com.lailaps.ui.ScreenType;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    public static String title = "lailaps";

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ScreenContainer screenContainer = new ScreenContainer();
        screenContainer.loadAllScreens();
        screenContainer.showScreen(ScreenType.LOGIN);

        Group root = new Group();
        root.getChildren().addAll(screenContainer);
        stage.setScene(new Scene(root));
        stage.show();

        Image icon = new Image(getClass().getClassLoader().getResourceAsStream("images/lailaps2_32x32.png"));
        stage.getIcons().add(icon);
        stage.setTitle(title);
    }
}