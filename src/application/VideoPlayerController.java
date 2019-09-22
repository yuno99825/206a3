package application;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class VideoPlayerController {

    @FXML
    private MediaView mediaView;

    public void setUpVideo(String creationName) {
        File videoURL = new File("./creations/" + creationName + "/creation.mp4");
        Media video = new Media(videoURL.toURI().toString());
        MediaPlayer player = new MediaPlayer(video);
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
    }


}
