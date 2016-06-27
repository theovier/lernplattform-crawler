package com.lailaps.ui;

import com.lailaps.Director;
import com.lailaps.login.LoginCredentials;
import com.lailaps.PreferencesManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class LoginWindow extends Window {

    private final static String[] EMAILS = {"@stud.hshl.de"};
    private JFormattedTextField userField, directoryField;
    private JPasswordField passwordField;
    private JButton btnLogin, btnBrowse;
    private JComboBox <String> emailList;
    private JFileChooser dirChooser;
    private JMenuBar menuBar;
    private JMenu helpMenu;
    private JMenuItem aboutItem, checkForUpdateItem;
    private String currentDir;
    private Director director;

    public LoginWindow() {
        super();
        configureDirectoryChooser();
        configureTextFields();
        configureMenus();
        initController();
    }

    protected void initWidgets() {
        super.initWidgets();
        btnLogin = new JButton("fetch");
        btnBrowse = new JButton("browse");
        emailList = new JComboBox <>(EMAILS);
        userField = new JFormattedTextField();
        directoryField = new JFormattedTextField();
        passwordField = new JPasswordField();
        dirChooser = new JFileChooser("Choose Directory");
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        checkForUpdateItem = new JMenuItem("Check for Updates");
        createHints();
    }

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

    protected void setWidgetPositions() {
        super.setWidgetPositions();
        userField.setBounds(125, 50, 150, 25);
        emailList.setBounds(280, 50, 150, 25);
        passwordField.setBounds(125, 100, 150, 25);
        btnLogin.setBounds(125, 150, 75, 20);
        btnBrowse.setBounds(400, 190, 75, 25);
        directoryField.setBounds(125, 190, 270, 25);
    }

    protected void initPanel() {
        super.initPanel();
        panel.setLayout(null);
    }

    protected void addPanelContent() {
        super.addPanelContent();
        panel.add(btnLogin);
        panel.add(userField);
        panel.add(passwordField);
        panel.add(emailList);
        panel.add(btnBrowse);
        panel.add(directoryField);
    }

    protected void initFrame() {
        super.initFrame();
        frame.setJMenuBar(menuBar);
        frame.setContentPane(panel);
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

    private void configureMenus() {
        helpMenu.setMnemonic('H');
        menuBar.add(helpMenu);
        helpMenu.add(aboutItem);
        helpMenu.add(checkForUpdateItem);
    }

    private void initController() {
        director = new Director(this);
    }

    private void createHints() {
        HintText usernameHint = new HintText(userField, "Theo.Harkenbusch");
        HintText passwordHint = new HintText(passwordField, "password");
    }

    private void startLogin() {
        LoginCredentials credentials = createCredentials();
        director.start(credentials);
    }

    private LoginCredentials createCredentials() {
        String user = userField.getText();
        String password = "";
        for (char c : passwordField.getPassword()) {
            password += c;
        }
        return new LoginCredentials(user, String.valueOf(emailList.getSelectedItem()), password);
    }
}
