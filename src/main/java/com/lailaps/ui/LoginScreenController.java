package com.lailaps.ui;

import com.lailaps.Director;
import com.lailaps.Main;
import com.lailaps.PreferencesManager;
import com.lailaps.login.LoginCredentials;
import com.lailaps.login.LoginErrorInterpreter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable, Controllable {

    private DirectoryChooser dirChooser = new DirectoryChooser();
    private String currentDir = PreferencesManager.getDirectory();
    private ScreenContainer parent;
    private ResourceBundle bundle;

    @FXML
    private TextField userField, directoryField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox <String> emailList;
    
    @FXML
    private ProgressIndicator throbber;


    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        this.bundle = rb;
        Platform.runLater(()->setInitialFocus());
        userField.setText(PreferencesManager.getUsername());
        directoryField.setText(currentDir);
        emailList.getSelectionModel().selectFirst();
    }

    @Override
    public void setParentScreen(ScreenContainer parent) {
        this.parent = parent;
    }

    @Override
    public ScreenContainer getParentScreen() {
        return parent;
    }

    @FXML
    private void handleLoginButtonAction(final ActionEvent e) {
        startLogin();
    }

    @FXML
    private void handleDirChooseButtonAction(final ActionEvent e) {
        dirChooser.setInitialDirectory(new java.io.File(currentDir));
        File file = dirChooser.showDialog(new Stage());
        if (file != null) {
            currentDir = file.toString() + File.separator + Main.title;  //TODO enable edit of dirfield?
            directoryField.setText(currentDir);
            PreferencesManager.setDirectory(currentDir);
        }
    }

    @FXML
    private void onEnterPressed(final ActionEvent e) {
        startLogin();
    }

    private void setInitialFocus() {
        if (PreferencesManager.hasUsernamePreference()) {
            passwordField.requestFocus();
        } else {
            emailList.requestFocus();
        }
    }

    private void startLogin() {
        if (!hasUsernameEntered()) {
           parent.showErrorPopup(
                   bundle.getString("misc.error"),
                   bundle.getString("login.error.blank.username"),
                   bundle.getString("login.error.blank.username.subtitle")
           );
        } else if (!hasPasswordEntered()) {
            parent.showErrorPopup(
                    bundle.getString("misc.error"),
                    bundle.getString("login.error.blank.password"),
                    bundle.getString("login.error.blank.password.subtitle")
            );
        } else {
            login();
        }
    }

    private boolean hasUsernameEntered() {
        return userField.getText().length() > 0;
    }

    private boolean hasPasswordEntered() {
        return passwordField.getText().length() > 0;
    }

    private void login() {
        throbber.setVisible(true);
        LoginCredentials credentials = createCredentials();
        Director director = new Director(this);
        director.start(credentials);
    }

    private LoginCredentials createCredentials() {
        String user = userField.getText();
        String password = passwordField.getText();
        return new LoginCredentials(user, emailList.getSelectionModel().getSelectedItem(), password);
    }

    public void showLoginError(Exception e) {
        throbber.setVisible(false);
        String errorTitle = bundle.getString("login.error.header");
        String message = LoginErrorInterpreter.getErrorMsg(e, bundle);
        String errorSubtitle = LoginErrorInterpreter.getErrorSubtitle(e, bundle);
        parent.showErrorPopup(errorTitle, message, errorSubtitle);
    }
}
