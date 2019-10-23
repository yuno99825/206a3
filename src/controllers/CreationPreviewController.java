package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.*;

public class CreationPreviewController {

    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    @FXML
    private Button replayButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField nameField;
    @FXML
    private Label nameErrorLabel;
    @FXML
    private void initialize() throws IOException{
        File videoURL = new File("./.temp/creation.mp4");
        Media video = new Media(videoURL.toURI().toString());
        player = new MediaPlayer(video);
        player.setOnEndOfMedia(() -> replayButton.setVisible(true));
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
    }

    @FXML
    private void confirmButtonClicked() throws InterruptedException, IOException {
        String creationName = nameField.getText();
        if (nameIsValid(creationName)) {
            player.pause();

            ProcessBuilder pb = new ProcessBuilder();
            pb.command("/bin/bash", "-c", "[ -d ./creations ]");
            Process checkFolderExist = pb.start();
            int folderExsists = checkFolderExist.waitFor();
            if (folderExsists != 0) {
                pb.command("/bin/bash", "-c", "mkdir creations");
                Process makeFolder = pb.start();
                makeFolder.waitFor();
            }

            pb = new ProcessBuilder();
            pb.command("/bin/bash", "-c", "[ -e ./creations/\"" + creationName + "\" ]");
            Process checkExist = pb.start();
            int creationExists = checkExist.waitFor();

            if (creationExists != 0) {
                pb.command("/bin/bash", "-c", "mv ./.temp ./creations/" + "\"" + creationName + "\"");
                Process moveTemp = pb.start();
                Stage thisStage = (Stage)nameField.getScene().getWindow();
                thisStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Creation: '" + creationName + "' already exists. Do you wish to overwrite it?", ButtonType.YES, ButtonType.NO);
                alert.setTitle("Creation already exists");
                alert.setHeight(150);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    pb.command("/bin/bash", "-c", "rm -fr ./creations/\"" + creationName + "\"");
                    Process removeExisting = pb.start();
                    removeExisting.waitFor();

                    pb.command("/bin/bash", "-c", "mv -f ./.temp ./creations/\"" + creationName + "\"");
                    pb.start();

                    Stage thisStage = (Stage)nameField.getScene().getWindow();
                    thisStage.close();
                }
            }
        } else {
            nameErrorLabel.setVisible(true);
        }
    }
    @FXML
    public void cancelButtonClicked() throws IOException {
        player.pause();
        // Create confirmation alert
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to cancel? All progress will be lost!.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Cancel Creation");
        alert.setHeight(150);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "rm -fr ./.temp");
            builder.start();
            Stage thisStage = (Stage)nameField.getScene().getWindow();
            thisStage.close();
        }
    }

    @FXML
    private void keyReleased(){
        String text = nameField.getText();
        boolean isEmpty = (text.isEmpty() || text.trim().isEmpty());
        confirmButton.setDisable(isEmpty);
    }

    @FXML
    private void playPauseVid(){
        if (player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.pause();
        }
        else {
            player.play();
        }
    }

    @FXML
    private void replayButtonClicked() {
        replayButton.setVisible(false);
        player.seek(Duration.ZERO);
        player.play();
    }

    private boolean nameIsValid(String creationName) {
        return creationName.matches("[A-Za-z0-9_\\-][A-Za-z0-9_\\- ]*");
    }

}
