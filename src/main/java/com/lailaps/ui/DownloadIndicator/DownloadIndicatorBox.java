package com.lailaps.ui.DownloadIndicator;


import javafx.scene.control.Control;
import javafx.scene.layout.HBox;

public abstract class DownloadIndicatorBox extends HBox {

    public abstract void bindProgressBarWidthProperty(Control parent);
}
