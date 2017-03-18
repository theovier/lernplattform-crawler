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
    private HashMap<String, Node> screens = new HashMap<>();

    public ScreenManager() {
        super();
    }

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public void unloadScreen(String name) {
        screens.remove(name);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    public boolean loadScreen(String name, String resource) {
        try {
            URL screenLocation = getClass().getClassLoader().getResource(resource);
            FXMLLoader loader = new FXMLLoader(screenLocation);
            Parent root = loader.load();
            //(ControlledScreen) loader.getController();
            //setScreenParent(this);
            addScreen(name, root);
            return true;
        } catch (IOException e) {
            LOG.error(e);
            return false;
        }
    }

    public boolean displayScreen(final String screenName) {
        if (!isScreenInitialized(screenName)) {
            LOG.warn(String.format("Can't display screen '%1' since it has not been initialized yet.", screenName));
            return false;
        }
        if (hasMultipleScreens()) {
            fadeToScreen(screenName);
        } else {
            showScreen(screenName);
        }
        return true;
    }

    private boolean isScreenInitialized(final String screenName) {
        return screens.get(screenName) == null;
    }

    private boolean hasMultipleScreens() {
        return !getChildren().isEmpty();
    }

    private void fadeToScreen(final String newScreenName) {
        /*
        final DoubleProperty opacity = opacityProperty();
        KeyValue startOpacity =  new KeyValue(opacity, 1.0);
        KeyValue endOpacity = new KeyValue(opacity, 0.0);
        KeyFrame visibleKeyFrame = new KeyFrame(Duration.ZERO, startOpacity);
        KeyFrame invisibleKeyFrame = new KeyFrame(FADEOUT_DURATION, (ActionEvent) -> onFadeOutFinish(screenName, opacity), endOpacity);
        Timeline fadeout = new Timeline(visibleKeyFrame, invisibleKeyFrame);
        fadeout.play();
        */

        playScreenFade(1, 0, FADEOUT_DURATION, (ActionEvent) -> onFadeOutFinish(newScreenName));

    }

    //todo rename
    private void showScreen(final String screenName) {
        getChildren().add(0, screens.get(screenName));
    }

    private void removeCurrentScreen() {
        getChildren().remove(0);
    }

    //private void onFadeOutFinish(String newScreenName, DoubleProperty opacity) {
    private void onFadeOutFinish(String newScreenName) {
       /* removeCurrentScreen();
        showScreen(newScreenName);
        KeyValue startOpacity =  new KeyValue(opacity, 0.0);
        KeyValue endOpacity = new KeyValue(opacity, 1.0);
        KeyFrame invisibleKeyFrame = new KeyFrame(Duration.ZERO, startOpacity);
        KeyFrame visibleKeyFrame = new KeyFrame(FADEIN_DURATION, endOpacity);
        Timeline fadeIn = new Timeline(invisibleKeyFrame, visibleKeyFrame);
        fadeIn.play();
        */

       playScreenFade(0, 1, FADEIN_DURATION, null);


    }

    private void playScreenFade(double startOpacity, double endOpacity, Duration fadeDuration, EventHandler<ActionEvent> onFinished) {
        final DoubleProperty opacity = opacityProperty();
        KeyValue startValue =  new KeyValue(opacity, startOpacity);
        KeyValue endValue = new KeyValue(opacity, endOpacity);
        KeyFrame firstKeyFrame = new KeyFrame(Duration.ZERO, startValue);
        KeyFrame lastKeyFrame;
        if (onFinished == null) {
            lastKeyFrame = new KeyFrame(fadeDuration, endValue);
        } else {
            lastKeyFrame = new KeyFrame(fadeDuration, onFinished, endValue);
        }
        Timeline fade = new Timeline(firstKeyFrame, lastKeyFrame);
        fade.play();
    }
}
