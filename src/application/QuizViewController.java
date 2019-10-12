package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuizViewController {

    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    @FXML
    private Button replayButton;
    @FXML
    private Button startButton;
    @FXML
    private Label questionNumberLabel;
    private List<String> _creationsList;
    private Stage _stage;
    private int _questionNumber = 0;


    public void setCreationsList(List<String> creationsList) {
        this._creationsList = creationsList;
    }

    public void setStage(Stage stage) {
        this._stage = stage;
    }

    @FXML
    private void initialize() {
        startButton.toFront();
    }

    private void playQuizMedia(){
        int numberOfCreations = _creationsList.size();
        int randomCreation = (int)(Math.random() * numberOfCreations);
        String creationToPlay = _creationsList.get(randomCreation);
        _questionNumber ++;
        questionNumberLabel.setText("Question " + _questionNumber + "...");

        File videoURL = new File("./creations/" + creationToPlay + "/creation.mp4");
        Media video = new Media(videoURL.toURI().toString());
        player = new MediaPlayer(video);
        player.setOnReady(() -> {
            _stage.sizeToScene();
        });
        player.setOnEndOfMedia(() -> replayButton.setVisible(true));
        player.setAutoPlay(true);
        mediaView.setMediaPlayer(player);
        mediaView.fitWidthProperty().bind(_stage.widthProperty());
        //mediaView.fitHeightProperty().bind(_stage.heightProperty());

        _creationsList.remove(randomCreation);

    }

    @FXML
    private void startButtonClicked(){
        startButton.setVisible(false);
        playQuizMedia();
    }

    @FXML
    private void playPauseVid(){
        if (player != null) {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                player.pause();
            } else {
                player.play();
            }
        }
    }

    @FXML
    private void replayButtonClicked() {
        replayButton.setVisible(false);
        player.seek(Duration.ZERO);
        player.play();
    }

    public void stopVideo() {
        if (player != null) {
            player.pause();
        }
    }
}
