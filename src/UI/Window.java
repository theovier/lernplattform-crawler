package UI;

import javax.swing.*;

public class Window {

    public static final String TITLE = "lailaps";
    public static final int WIDTH = 500;
    public static final int HEIGHT = 250;
    public static final String[] EMAILS = {"@stud.hshl.de", "@hshl.de"};

    private JFrame frame;
    private JPanel panel;
    private JFormattedTextField usernameField;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JComboBox emailList;

    public Window() {
        initWidgets();
        setWidgetPositions();
        initPanel();
        addPanelContent();
        initFrame();
        createHints();
        btnLogin.grabFocus();
    }

    private void initWidgets() {
        btnLogin = new JButton("login");
        emailList = new JComboBox(EMAILS);
        usernameField = new JFormattedTextField();
        passwordField = new JPasswordField();
        createHints();
    }

    private void setWidgetPositions() {
        usernameField.setBounds(125, 50, 150, 25);
        emailList.setBounds(280, 50, 150, 25);
        passwordField.setBounds(125, 100, 150, 25);
        btnLogin.setBounds(125, 150, 75, 20);
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(null);
    }

    private void addPanelContent() {
        panel.add(btnLogin);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(emailList);
    }

    private void initFrame() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void createHints() {
        HintText usernameHint = new HintText(usernameField, "username");
        HintText passwordHint = new HintText(passwordField, "password");
    }
}
