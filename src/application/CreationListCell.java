package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CreationListCell {
    @FXML
    private Label creationNameLabel;
    @FXML
    private HBox hBox;

    public void setText(String text) {
        creationNameLabel.setText(text);
    }

}
