package com.lailaps;

import com.lailaps.ui.AboutAlert;
import com.lailaps.ui.ScreenContainer;
import com.lailaps.ui.ScreenType;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ResourceBundle;


public class Main extends Application {

    public static String title = "lailaps";

    private Stage stage;
    private final HostServices HOST_SERVICES = getHostServices();
    private final ResourceBundle BUNDLE = ResourceBundle.getBundle("languages.UIResources");
    private static final String ICON_FILE = "images/lailaps_32x32.png";
    private ScreenContainer screenContainer = new ScreenContainer(BUNDLE);
    private MenuItem aboutItem = new MenuItem(BUNDLE.getString("menu.about"));
    private Menu helpMenu = new Menu(BUNDLE.getString("menu.help"), null, aboutItem);
    private MenuBar menuBar = new MenuBar(helpMenu);
    private Group root = new Group();

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(final Stage stage) {
        this.stage = stage;
        prepareMenus();

        screenContainer.loadAllScreens();

        root.getChildren().addAll(screenContainer, menuBar);

        stage.setTitle(title);
        stage.getIcons().add(getIcon());
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.setScene(new Scene(root));

        screenContainer.showScreen(ScreenType.LOGIN);
        stage.show();
    }

    private void prepareMenus() {
        enableMnemonicParsing();
        setMenuBarLayout();
        setMenuItemActions();
    }

    private void setMenuBarLayout() {
        menuBar.setPadding(new Insets(0,0,0,0));
        menuBar.prefWidthProperty().bind(stage.widthProperty());
    }

    private void setMenuItemActions() {
        aboutItem.setOnAction( (e) -> showAboutAlert());
    }

    private void showAboutAlert() {
        AboutAlert aboutWindow = new AboutAlert(HOST_SERVICES, BUNDLE);
        aboutWindow.initOwner(stage);
        aboutWindow.show();
    }

    private void enableMnemonicParsing() {
        helpMenu.setMnemonicParsing(true);
        aboutItem.setMnemonicParsing(true);
    }

    private Image getIcon() {
        return new Image(getClass().getClassLoader().getResourceAsStream(ICON_FILE));
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    //TODO wenn Kurs leer -> trotzdem ordner erstellen?

}