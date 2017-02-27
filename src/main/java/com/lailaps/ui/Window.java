package com.lailaps.ui;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public abstract class Window {

    private static final Logger LOG = Logger.getLogger(Window.class);

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
        setWidgetPositions();
        configureMenus();
        initPanel();
        addPanelContent();
        initFrame();
        setListeners();
        setIcon();
    }

    protected void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.getDefaults().put("Button.showMnemonics", Boolean.TRUE);
        }
        catch (Exception e) {
            LOG.warn("look and feel could not be changed!");
        }
    }

    protected void initWidgets() {
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        checkForUpdateItem = new JMenuItem("Check for Updates");
    }

    protected void setInitialFocusOnWidget() {
        //no need to force subclasses to inherit it via abstract
    }

    abstract void setWidgetPositions();

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

    abstract void addPanelContent();

    protected void initFrame() {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        frame.setJMenuBar(menuBar);
    }

    protected void setListeners() {
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowOpened( WindowEvent e){
                SwingUtilities.invokeLater( () ->
                        setInitialFocusOnWidget()
                );
            }
        });
    }

    private void setIcon()  {
        try {
            Image icon = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/lailaps2_32x32.png"));
            frame.setIconImage(icon);
        } catch (IOException e) {
            LOG.warn("IconImage could not be set.");
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }
}
