package com.lailaps.ui;

import com.bric.plaf.AquaThrobberUI;
import com.bric.plaf.ThrobberUI;
import com.bric.swing.JThrobber;
import com.lailaps.Director;
import com.lailaps.login.LoginCredentials;
import com.lailaps.PreferencesManager;
import com.lailaps.login.LoginErrorInterpreter;
import com.lailaps.login.WrongCredentialsException;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class LoginWindow extends Window {

    private static final Logger LOG = Logger.getLogger(LoginWindow.class);
    private final static String[] EMAILS = {"@stud.hshl.de"};
    private JFormattedTextField userField, directoryField;
    private JPasswordField passwordField;
    private JButton btnLogin, btnBrowse;
    private JComboBox <String> emailList;
    private JFileChooser dirChooser;
    private String currentDir;
    private JThrobber throbber;
    private JLabel errorLabel;

    public LoginWindow() {
        super();
        configureDirectoryChooser();
        configureTextFields();
        configureErrorLabel();
        configureThrobber();
        createHints();
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        btnLogin = new JButton("fetch");
        btnBrowse = new JButton("browse");
        emailList = new JComboBox <>(EMAILS);
        userField = new JFormattedTextField();
        directoryField = new JFormattedTextField();
        passwordField = new JPasswordField();
        dirChooser = new JFileChooser("Choose Directory");
        throbber = new JThrobber();
        errorLabel = new JLabel();
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        btnLogin.addActionListener(e -> startLogin());
        btnLogin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    startLogin();
            }
        });
        btnBrowse.addActionListener(e -> {
            dirChooser.setCurrentDirectory(new java.io.File(currentDir));

            //todo extract method?
            if (dirChooser.showOpenDialog(super.panel) == JFileChooser.APPROVE_OPTION) {
                currentDir = dirChooser.getSelectedFile().toString();
                directoryField.setText(currentDir);
                PreferencesManager.getInstance().setDirectory(currentDir);
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    startLogin();
            }
        });
        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    startLogin();
            }
        });
    }

    @Override
    protected void setWidgetPositions() {
        userField.setBounds(125, 50, 150, 25);
        emailList.setBounds(280, 50, 150, 25);
        passwordField.setBounds(125, 100, 150, 25);
        btnLogin.setBounds(125, 150, 75, 20);
        btnBrowse.setBounds(400, 190, 75, 25);
        directoryField.setBounds(125, 190, 270, 25);
        throbber.setBounds(50, 75, 50, 50);
        errorLabel.setBounds(280,100,150,25);
    }

    @Override
    protected void initPanel() {
        super.initPanel();
        panel.setLayout(null);
    }

    @Override
    protected void addPanelContent() {
        panel.add(btnLogin);
        panel.add(userField);
        panel.add(passwordField);
        panel.add(emailList);
        panel.add(btnBrowse);
        panel.add(directoryField);
        panel.add(throbber);
        panel.add(errorLabel);
    }

    @Override
    protected void initFrame() {
        super.initFrame();
        frame.setContentPane(panel);
    }

    @Override
    protected void setInitialFocusOnWidget() {
        //focus on password when the user once logged in successfully
        if (PreferencesManager.hasUsernamePreference()) {
            passwordField.requestFocusInWindow();
        }
    }

    private void configureDirectoryChooser() {
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);
        currentDir = PreferencesManager.getInstance().getDirectory();
    }

    private void configureTextFields() {
        directoryField.setEditable(false);
        directoryField.setText(currentDir);
    }

    private void configureThrobber() {
        throbber.setUI(new AquaThrobberUI());
        throbber.putClientProperty(ThrobberUI.PERIOD_MULTIPLIER_KEY, 2);
        throbber.setActive(false);
    }

    private void configureErrorLabel() {
        errorLabel.setVisible(false);
        errorLabel.setForeground(Color.red);
    }

    private void createHints() {
        userField.setText(PreferencesManager.getUsername());

        //todo rewrite
        //HintText usernameHint = new HintText(userField, "Theo.Harkenbusch");
        //HintText passwordHint = new HintText(passwordField, "password");
    }

    private void startLogin() {
        throbber.setActive(true);
        errorLabel.setVisible(false);
        LoginCredentials credentials = createCredentials();
        Director director = new Director(this);
        director.start(credentials);
    }

    private LoginCredentials createCredentials() {
        String user = userField.getText();
        StringBuilder passwordBuilder = new StringBuilder();
        for (char c : passwordField.getPassword()) {
            passwordBuilder.append(c);
        }
        String password = passwordBuilder.toString();
        return new LoginCredentials(user, emailList.getSelectedItem().toString(), password);
    }

    public void showLoginError(Exception e) {
        String error = LoginErrorInterpreter.getErrorMsg(e);
        errorLabel.setText(error);
        errorLabel.setVisible(true);
        throbber.setActive(false);
    }
}
