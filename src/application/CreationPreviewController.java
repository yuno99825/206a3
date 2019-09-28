package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CreationPreviewController {

    @FXML
    private Button confirmButton;
    @FXML
    private TextField nameField;

    public void initialize(){
        confirmButton.setDisable(true);
    }

    public void confirmButtonClicked(){
        String creationName = nameField.getText();
        int exit = 0;
        String cmd1 = "[ -e ./creations/" + creationName + " ]";
        try {
            ProcessBuilder builder1 = new ProcessBuilder("/bin/bash", "-c", cmd1);
            Process process1 = builder1.start();
            exit = process1.waitFor();
        } catch( Exception e) {
        }
       if (exit == 1) {
           String cmd = "mv ./.temp ./creations/" + creationName;
           try {
               ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
               Process process = builder.start();
           } catch (Exception e) {

           }
           Stage thisStage = (Stage)nameField.getScene().getWindow();
           thisStage.close();
       } else {
           Alert alert = new Alert(Alert.AlertType.NONE, "Creation: " + creationName + " already exists. Do you wish to overwrite it?", ButtonType.YES, ButtonType.NO);
           alert.setTitle("Creation already exists");
           alert.setHeight(150);
           alert.showAndWait();

           //
           if (alert.getResult() == ButtonType.YES) {
               String cmd = "mv -f ./.temp ./creations/" + creationName;
               try {
                   ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
                   Process process = builder.start();
               } catch (Exception e) {

               }
               Stage thisStage = (Stage)nameField.getScene().getWindow();
               thisStage.close();
           }
       }

    }


    public void disableButton(){
        String text = nameField.getText();
        boolean isEmpty = (text.isEmpty() || text.trim().isEmpty());
        confirmButton.setDisable(isEmpty);
    }


}
