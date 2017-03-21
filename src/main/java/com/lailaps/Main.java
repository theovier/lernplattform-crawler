package com.lailaps;

import com.lailaps.ui.ScreenContainer;
import com.lailaps.ui.ScreenType;
import javafx.application.Application;
import javafx.application.Platform;
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

        stage.setOnCloseRequest(e -> Platform.exit());
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }


    //TODO check admin rights for preferences -> speichertort wird durch preferences bestimmt?!
    //TODO wenn ordner leer, meldung geben und schließen
    //TODO Exception in thread "Thread-6" java.lang.ClassCastException: com.gargoylesoftware.htmlunit.UnexpectedPage cannot be cast to com.gargoylesoftware.htmlunit.html.HtmlPage
    //TODO ORDNER in LERNPLATTFORM!

    //Request URL:https://campusapp01.hshl.de/pluginfile.php/143371/mod_resource/content/1/Vorlesung%201%20-%20Haeufigkeiten.ipynb?forcedownload=1
    //Content-Disposition:attachment; filename="Vorlesung 1 - Haeufigkeiten.ipynb"
    //Content-Range:bytes 3274-3274/89120     //89120 Bytes!
}