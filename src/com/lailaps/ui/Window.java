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
    protected JMenuBar menuBar;
    protected JMenu helpMenu;
    protected JMenuItem aboutItem, checkForUpdateItem;

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
        configureMenus();
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
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        checkForUpdateItem = new JMenuItem("Check for Updates");
    }

    protected void setListeners() {

    }

    protected void setWidgetPositions() {

    }

    protected void configureMenus() {
        helpMenu.setMnemonic('H');
        menuBar.add(helpMenu);
        helpMenu.add(aboutItem);
        helpMenu.add(checkForUpdateItem);
        //todo add new window which pops up at "aboutItem"
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
        frame.setJMenuBar(menuBar);
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
