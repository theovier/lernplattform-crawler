package com.lailaps.ui;

import com.lailaps.Director;
import com.lailaps.login.LoginCredentials;
import com.lailaps.PreferencesManager;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Window {

    public static final String TITLE = "lailaps";
    public static final int WIDTH = 500;
    public static final int HEIGHT = 250;
    public static final String[] EMAILS = {"@stud.hshl.de", "@hshl.de"};

    private JFrame frame;
    private JPanel panel;
    private JFormattedTextField userField, directoryField;
    private JPasswordField passwordField;
    private JButton btnLogin, btnBrowse;
    private JComboBox <String> emailList;
    private JFileChooser dirChooser;
    private String currentDir;
    private Director director;

    public Window() {
        setLookAndFeel();
        initWidgets();
        setListeners();
        setWidgetPositions();
        configureDirectoryChooser();
        configureTextFields();
        initPanel();
        addPanelContent();
        initFrame();
        initController();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            //won't happen
        }
    }

    private void initWidgets() {
        btnLogin = new JButton("fetch");
        btnBrowse = new JButton("browse");
        emailList = new JComboBox <>(EMAILS);
        userField = new JFormattedTextField();
        directoryField = new JFormattedTextField();
        passwordField = new JPasswordField();
        dirChooser = new JFileChooser("Choose Directory");
        createHints();
    }

    private void setListeners() {
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
            if (dirChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
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

    private void setWidgetPositions() {
        userField.setBounds(125, 50, 150, 25);
        emailList.setBounds(280, 50, 150, 25);
        passwordField.setBounds(125, 100, 150, 25);
        btnLogin.setBounds(125, 150, 75, 20);
        btnBrowse.setBounds(400, 190, 75, 25);
        directoryField.setBounds(125, 190, 270, 25);
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

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(null);
    }

    private void addPanelContent() {
        panel.add(btnLogin);
        panel.add(userField);
        panel.add(passwordField);
        panel.add(emailList);
        panel.add(btnBrowse);
        panel.add(directoryField);
    }

    private void initFrame() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
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

    public void show() {
        frame.setVisible(true);
    }
}