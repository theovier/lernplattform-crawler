package com.lailaps.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Window {

    public static String title = "lailaps";
    public static int width = 650;
    public static int height = 350;

    protected JFrame frame;
    protected JPanel panel;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        prepareWindow();
    }

    public Window() {
        prepareWindow();
    }

    private void prepareWindow() {
        setLookAndFeel();
        initWidgets();
        setListeners();
        setWidgetPositions();
        initPanel();
        addPanelContent();
        initFrame();
        setIcon();
    }

    protected void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.getDefaults().put("Button.showMnemonics", Boolean.TRUE);
        }
        catch (Exception e) {
            //won't happen
        }
    }

    protected void initWidgets() {

    }

    protected void setListeners() {

    }

    protected void setWidgetPositions() {

    }

    protected void initPanel() {
        panel = new JPanel();
    }

    protected void addPanelContent() {

    }

    protected void initFrame() {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
    }

    private void setIcon()  {
        try {
            frame.setIconImage(ImageIO.read(getClass().getResource("/resources/images/lailaps2_32x32.png")));
        } catch (IOException e) {

        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }
}
