package application.scenes;

import application.PrimaryScene;
import application.SceneType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.*;

/**
 * The FXML controller for the scene in which users preview their creation to confirm it is correct and then name it.
 * Handles the application logic of this scene, including:
 * - play/pause/replay of creation
 * - naming creation
 * - saving creation
 */

public class CreationPreviewController extends PrimaryScene {
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

    /**
     * Plays the creation currently in the .temp folder.
     * This is the creation that the user is currently making.
     */
    @FXML
    private void initialize() throws IOException {
        File videoURL = new File("./.temp/creation.mp4");
        Media video = new Media(videoURL.toURI().toString());
        player = new MediaPlayer(video);
        player.setOnEndOfMedia(() -> replayButton.setVisible(true));
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
    }

    /**
     * Attempts to confirm creation by moving the .temp folder to the creations folder under the name specified by the user.
     * If the name is not valid, a warning is displayed.
     * If a creation with the same name already exists, it allows the user overwrite it.
     */
    @FXML
    private void confirmButtonClicked() throws InterruptedException, IOException {
        String creationName = nameField.getText();
        if (nameIsValid(creationName)) {
            player.pause();
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("/bin/bash", "-c", "bash ./resources/scripts/moveTempFolder.sh \"" + creationName + "\"");
            Process process = pb.start();
            int exitStatus = process.waitFor();
            if (exitStatus == 1) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Creation: '" + creationName + "' already exists. Do you wish to overwrite it?", ButtonType.YES, ButtonType.NO);
                alert.setTitle("Creation already exists");
                alert.setHeight(150);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    // delete existing creation
                    pb.command("/bin/bash", "-c", "rm -fr ./creations/\"" + creationName + "\"");
                    Process removeExisting = pb.start();
                    removeExisting.waitFor();

                    pb.command("/bin/bash", "-c", "mv -f ./.temp ./creations/\"" + creationName + "\"");
                    Process moveTemp = pb.start();
                    moveTemp.waitFor();
                    setScene(SceneType.MENU, stage);
                }
            } else {
                setScene(SceneType.MENU, stage);
            }
        } else {
            nameErrorLabel.setVisible(true);
        }
    }

    /**
     * Attempts to return to the home screen.
     * A warning that all progress will be lost is shown first.
     */
    @FXML
    public void cancelButtonClicked() throws IOException, InterruptedException {
        player.pause();
        // Create confirmation alert
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to cancel? All progress will be lost!.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Cancel Creation");
        alert.setHeight(150);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "rm -fr ./.temp");
            Process delete = builder.start();
            delete.waitFor();
            setScene(SceneType.MENU, stage);
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
