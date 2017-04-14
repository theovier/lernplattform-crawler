package com.lailaps.ui;


import com.lailaps.crawler.Term;
import javafx.scene.control.CheckBox;

public class TermCheckBox extends CheckBox {

    private final Term TERM;

    public TermCheckBox(final Term term) {
        super(term.toString());
        this.TERM = term;
    }

    public Term getTerm() {
        return TERM;
    }
}
