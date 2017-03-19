package com.lailaps.ui;

import com.lailaps.ScreenType;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ScreenManager extends StackPane {

    private static final Logger LOG = Logger.getLogger(ScreenManager.class);

    private final Duration FADEOUT_DURATION = new Duration(1000);
    private final Duration FADEIN_DURATION = new Duration(800);
    private HashMap<ScreenType, Node> screens = new HashMap<>();

    public ScreenManager() {
        super();
    }

    public boolean loadScreen(ScreenType name, String resource) {
        try {
            URL screenLocation = getClass().getClassLoader().getResource(resource);
            FXMLLoader loader = new FXMLLoader(screenLocation);
            Parent root = loader.load();
            Controllable controller = loader.getController();
            controller.setParentScreen(this);
            addScreen(name, root);
            return true;
        } catch (IOException e) {
            LOG.error(e);
            return false;
        }
    }

    public void addScreen(ScreenType name, Node screen) {
        screens.put(name, screen);
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
}
