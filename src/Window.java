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
    private JFormattedTextField userField;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JComboBox <String> emailList;

    public Window() {
        initWidgets();
        setListeners();
        setWidgetPositions();
        initPanel();
        addPanelContent();
        initFrame();
        btnLogin.grabFocus();
    }

    private void initWidgets() {
        btnLogin = new JButton("login");
        emailList = new JComboBox <String>(EMAILS);
        userField = new JFormattedTextField();
        passwordField = new JPasswordField();
        createHints();
    }

    private void setListeners() {
        btnLogin.addActionListener(e -> callLogin());
        btnLogin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    callLogin();
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    callLogin();
            }
        });
        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    callLogin();
            }
        });
    }

    private void setWidgetPositions() {
        userField.setBounds(125, 50, 150, 25);
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
        panel.add(userField);
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
        HintText usernameHint = new HintText(userField, "Theo.Harkenbusch");
        HintText passwordHint = new HintText(passwordField, "password");
    }

    private void callLogin() {
        Main.startLogin(createCredentials());
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
