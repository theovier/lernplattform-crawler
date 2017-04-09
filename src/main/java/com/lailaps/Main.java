package com.lailaps;

import com.lailaps.ui.ScreenContainer;
import com.lailaps.ui.ScreenType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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

        //menu stuff
        MenuBar menubar = new MenuBar();
        Menu helpMenu = new Menu("_Help");
        helpMenu.setMnemonicParsing(true);
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().add(aboutItem);
        menubar.getMenus().add(helpMenu);
        menubar.prefWidthProperty().bind(stage.widthProperty());
        menubar.setPadding(new Insets(0,0,0,0));

        Group root = new Group();
        root.getChildren().addAll(screenContainer, menubar);

        stage.setScene(new Scene(root));
        stage.show();

        Image icon = new Image(getClass().getClassLoader().getResourceAsStream("images/lailaps_32x32.png"));
        stage.getIcons().add(icon);
        stage.setTitle(title);

        stage.setOnCloseRequest(e -> Platform.exit());
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }


    //TODO own class for screen stuff
    //TODO ORDNER in LERNPLATTFORM!
}