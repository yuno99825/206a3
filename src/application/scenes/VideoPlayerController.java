package application.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

/**
 * FXML controller class for the scene in which users play existing creations.
 * Handles the application logic of this scene, including play/pause/replay of creations.
 */
public class VideoPlayerController {
    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    @FXML
    private Button replayButton;

    /**
     * Sets up the specified creation to play. The video is resized to fit the window and is stopped upon the window being closed.
     * @param creationName The name of the creation to play
     * @param stage The window in which the creation is played
     */
    public void setUpVideo(String creationName, Stage stage) {
        File videoURL = new File("./creations/" + creationName + "/creation.mp4");
        Media video = new Media(videoURL.toURI().toString());
        player = new MediaPlayer(video);
        player.setOnReady(() -> {
            stage.sizeToScene();
        });
        player.setOnEndOfMedia(() -> replayButton.setVisible(true));
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
        mediaView.fitWidthProperty().bind(stage.widthProperty());
        mediaView.fitHeightProperty().bind(stage.heightProperty());
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

    public void stopVideo() {
        player.pause();
    }

}
