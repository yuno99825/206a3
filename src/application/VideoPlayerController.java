package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class VideoPlayerController {

    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    @FXML
    private Button playPauseButton;

    public void setUpVideo(String creationName, Stage stage) {
        File videoURL = new File("./creations/" + creationName + "/creation.mp4");
        Media video = new Media(videoURL.toURI().toString());
        player = new MediaPlayer(video);
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
        mediaView.fitWidthProperty().bind(stage.widthProperty());
        mediaView.fitHeightProperty().bind(stage.heightProperty());
    }

    @FXML
    public void playPauseVid(){
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
