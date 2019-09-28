package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CreationPreviewController {

    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField nameField;
    @FXML
    private Button cancelButton;

    @FXML
    private void initialize(){
        confirmButton.setDisable(true);
        File videoURL = new File("./.temp/creation.mp4");
        Media video = new Media(videoURL.toURI().toString());
        player = new MediaPlayer(video);
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
    }

    @FXML
    private void confirmButtonClicked(){
        String creationName = nameField.getText();
        int exit = 0;
        String cmd1 = "[ -e ./creations/\"" + creationName + "\" ]";
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
           Alert alert = new Alert(Alert.AlertType.NONE, "Creation: '\"" + creationName + "\"' already exists. Do you wish to overwrite it?", ButtonType.YES, ButtonType.NO);
           alert.setTitle("Creation already exists");
           alert.setHeight(150);
           alert.showAndWait();

           //
           if (alert.getResult() == ButtonType.YES) {
               String cmd3 = "rm -fr ./creations/\"" + creationName + "\"";
               try {
                   ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd3);
                   Process process = builder.start();
               } catch (Exception e) {

               }
               String cmd = "mv -f ./.temp ./creations/\"" + creationName + "\"";
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
    @FXML
    private void cancelButtonClicked(){
        String cmd3 = "rm -fr ./.temp";
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd3);
            Process process = builder.start();
        } catch (Exception e) {

        }
        Stage thisStage = (Stage)nameField.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    private void disableButton(){
        String text = nameField.getText();
        boolean isEmpty = (text.isEmpty() || text.trim().isEmpty());
        confirmButton.setDisable(isEmpty);
    }

    @FXML
    private void playPauseVid(){
        if (player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.pause();
            playPauseButton.setText("Play");
        }
        else {
            player.play();
            playPauseButton.setText("Pause");
        }
    }

}
