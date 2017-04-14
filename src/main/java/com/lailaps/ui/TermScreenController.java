package com.lailaps.ui;


import com.lailaps.Director;
import com.lailaps.crawler.Term;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class TermScreenController implements Initializable, Controllable, AutoResizable{

    private ScreenContainer parent;
    private ObservableList<TermCheckBox> termCheckBoxes = FXCollections.observableArrayList();
    private Director director;

    @FXML
    private BorderPane pane;

    @FXML
    private CheckBox selectAllCheckBox;

    @FXML
    private ListView<TermCheckBox> listView;

    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        determineTimeToBindSizes();
        listView.setItems(termCheckBoxes);
    }

    public void setTerms(final List<Term> terms) {
        for (Term term : terms) {
            TermCheckBox box = new TermCheckBox(term);
            box.setOnAction(this::onAnyTermBoxClicked);
            termCheckBoxes.add(box);
        }
        highlightMostRecentTerm();
    }

    private void highlightMostRecentTerm() {
        if (!termCheckBoxes.isEmpty()) {
            TermCheckBox currentTerm = termCheckBoxes.get(0);
            currentTerm.setText(currentTerm.getText() + " (current term)"); //todo anmerkung aus unsichtbarem lokalisierten label ziehen?
            currentTerm.setSelected(true);
        }
    }

    public void setDirector(final Director director) {
        this.director = director;
    }

    @Override
    public void setParentScreen(ScreenContainer parent) {
        this.parent = parent;
    }

    @Override
    public ScreenContainer getParentScreen() {
        return parent;
    }

    @Override
    public void bindComponentsToStageSize() {
        Stage stage = (Stage) pane.getScene().getWindow();
        pane.prefWidthProperty().bind(stage.widthProperty().subtract(25));
        listView.prefWidthProperty().bind(stage.widthProperty());
        pane.prefHeightProperty().bind(stage.heightProperty().subtract(50));
    }

    @Override
    public void determineTimeToBindSizes() {
        pane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                bindComponentsToStageSize();
            }
        });
    }

    @FXML
    private void handleDownloadButtonAction(final ActionEvent e) {
        List<Term> selectedTerms = getSelectedTerms();
        if (selectedTerms.isEmpty()) {
            parent.showErrorPopup("Error", "Term Selection can't be blank.","Please select a term to download");
        } else {
            director.startDownloading(getSelectedTerms());
        }
    }

    private List<Term> getSelectedTerms() {
        List<Term> selectedTerms = new ArrayList<>(termCheckBoxes.size());
        for (TermCheckBox termBox : termCheckBoxes) {
            if (termBox.isSelected()) {
                selectedTerms.add(termBox.getTerm());
            }
        }
        return selectedTerms;
    }

    @FXML
    private void handleSelectAllAction(final ActionEvent e) {
        boolean selected = selectAllCheckBox.isSelected();
        for (CheckBox term : termCheckBoxes) {
            term.setSelected(selected);
        }
    }

    private void onAnyTermBoxClicked(final ActionEvent e) {
        selectAllCheckBox.setSelected(allTermBoxesSelected());
    }

    private boolean allTermBoxesSelected() {
        for (TermCheckBox termBox : termCheckBoxes)
            if (!termBox.isSelected()) return false;
        return true;
    }
}
