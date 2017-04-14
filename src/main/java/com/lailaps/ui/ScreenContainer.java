package com.lailaps.ui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class ScreenContainer extends StackPane {

    private static final Logger LOG = Logger.getLogger(ScreenContainer.class);
    private static final String LOGIN_SCREEN_FILE = "fxml/login.fxml";
    private static final String DOWNLOAD_SCREEN_FILE = "fxml/download.fxml";
    private static final String TERM_SCREEN_FILE = "fxml/term.fxml";
    private final Duration FADEOUT_DURATION = new Duration(1000);
    private final Duration FADEIN_DURATION = new Duration(800);
    private HashMap<ScreenType, Node> screens = new HashMap<>();
    private HashMap<ScreenType, Controllable> screenControllers = new HashMap<>();
    private ScreenType currentScreenType;
    private ResourceBundle bundle;

    public ScreenContainer(final ResourceBundle bundle) {
        super();
        this.bundle = bundle;
    }

    public void loadAllScreens() {
        loadScreen(ScreenType.LOGIN, LOGIN_SCREEN_FILE);
        loadScreen(ScreenType.TERM, TERM_SCREEN_FILE);
        loadScreen(ScreenType.DOWNLOAD, DOWNLOAD_SCREEN_FILE);
    }

    public boolean loadScreen(ScreenType name, String resource) {
        try {
            URL screenLocation = getClass().getClassLoader().getResource(resource);
            FXMLLoader loader = new FXMLLoader(screenLocation, bundle);
            Parent root = loader.load();
            Controllable controller = loader.getController();
            controller.setParentScreen(this);
            screenControllers.put(name, controller);
            addScreen(name, root);
            return true;
        } catch (IOException e) {
            LOG.error(e);
            return false;
        }
    }

    private void addScreen(ScreenType name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(ScreenType name) {
        return screens.get(name);
    }

    public Node getCurrentScreen() {
        return getScreen(currentScreenType);
    }

    public Window getCurrentWindow() {
        return getCurrentScreen().getScene().getWindow();
    }

    public boolean showScreen(final ScreenType screenName) {
        if (screenNotLoaded(screenName)) {
            LOG.warn(String.format("Can't display screen '%s' since it has not been initialized yet.", screenName));
            return false;
        }
        if (hasMultipleScreens()) {
            fadeToScreen(screenName);
        } else {
            displayScreenDirectly(screenName);
        }
        currentScreenType = screenName;
        return true;
    }

    private boolean screenNotLoaded(final ScreenType screenName) {
        return screens.get(screenName) == null;
    }

    private boolean hasMultipleScreens() {
        return !getChildren().isEmpty();
    }

    private void displayScreenDirectly(final ScreenType screenName) {
        getChildren().add(0, screens.get(screenName));
    }

    private void removeCurrentScreen() {
        getChildren().remove(0);
    }

    private void fadeToScreen(final ScreenType newScreenName) {
        fadeOut((ActionEvent) -> fadeIn(newScreenName));
    }

    private void fadeOut(EventHandler<ActionEvent> onFinished) {
        playScreenFade(1, 0, FADEOUT_DURATION, onFinished);
    }

    private void fadeIn(ScreenType newScreenName) {
        removeCurrentScreen();
        displayScreenDirectly(newScreenName);
        playScreenFade(0, 1, FADEIN_DURATION, null);
    }

    private void playScreenFade(double startOpacity, double endOpacity, Duration fadeDuration, EventHandler<ActionEvent> onFinished) {
        final DoubleProperty opacity = opacityProperty();
        KeyValue startValue =  new KeyValue(opacity, startOpacity);
        KeyValue endValue = new KeyValue(opacity, endOpacity);
        KeyFrame firstKeyFrame = new KeyFrame(Duration.ZERO, startValue);
        KeyFrame lastKeyFrame = new KeyFrame(fadeDuration, onFinished, endValue);
        Timeline fade = new Timeline(firstKeyFrame, lastKeyFrame);
        fade.play();
    }

    public Controllable getScreenController(ScreenType name) {
        return screenControllers.get(name);
    }

    //todo get also from resourcebundle
    public void showErrorPopup(String titleKey, String headerKey, String messageKey) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titleKey);
        alert.setHeaderText(headerKey);
        alert.setContentText(messageKey);
        alert.showAndWait();
    }
}
