package application.scenes.quiz;

import application.PrimaryScene;
import application.SceneType;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuizViewController extends PrimaryScene {

    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    @FXML
    private Button replayButton;
    @FXML
    private Button startButton;
    @FXML
    private Label questionLabel;
    @FXML
    private Button submitButton;
    @FXML
    private TextField answerField;
    @FXML
    private Label correctLabel;
    @FXML
    private Label wrongLabel;

    @FXML
    private Label labelWithAnswer;

    private List<String> _creationsList;
    private List<String> _creationsInOrder = new ArrayList<String>();
    private List<String> _userAnswers = new ArrayList<String>();
    private int _questionNumber = 0;
    private String _creationToPlay;
    private String _searchTerm = "";
    private int numberOfCorrect = 0;
    private int attemptNumber = 1;

    public void setCreationsList(List<String> creationsList) {
        _creationsList = creationsList;
    }

    @FXML
    private void startButtonClicked(){
        startButton.setVisible(false);
        answerField.setVisible(true);
        submitButton.setVisible(true);
        startQuiz();
    }

    private void startQuiz() {
        int numberOfCreations = _creationsList.size();
        questionLabel.setVisible(true);
        if (numberOfCreations > 0) {
            if (!(mediaView.isVisible())) {
                mediaView.setVisible(true);
            }
            int randomCreation = (int) (Math.random() * numberOfCreations);
            _creationToPlay = _creationsList.get(randomCreation);
            _searchTerm = getSearchTerm();
            _creationsInOrder.add(_searchTerm);
            _questionNumber++;
            questionLabel.setText("Question " + _questionNumber + "!");

            File videoURL = new File("./creations/" + _creationToPlay + "/quiz.mp4");
            Media video = new Media(videoURL.toURI().toString());
            player = new MediaPlayer(video);
            player.setOnEndOfMedia(() -> {
                replayButton.toFront();
                replayButton.setVisible(true);
            });
            player.setAutoPlay(true);
            mediaView.setMediaPlayer(player);

            List<String> temp = new ArrayList<String>();
            for (int i = 0; i < _creationsList.size(); i++) {
                if (!(_creationsList.get(i).equals(_creationToPlay))) {
                        temp.add(_creationsList.get(i));
                }
            }
            _creationsList = temp;
        } else {
           dispStatsScreen();
        }
    }

    private void dispStatsScreen(){
        try {
            QuizStatsController controller = (QuizStatsController) setScene(SceneType.QUIZ_STATS, stage);
            controller.setNumberOfCorrect(numberOfCorrect);
            controller.setQuestionNumber(_questionNumber);
            controller.setCreationsInOrder(_creationsInOrder);
            controller.setUserAnswers(_userAnswers);
            controller.setStats();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getSearchTerm(){
        String cmd = "cat ./creations/\"" + _creationToPlay + "\"/searchTerm.txt";
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream stdout = process.getInputStream();
        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
        String line;
        String term = "";try {
            while ((line = stdoutBuffered.readLine()) != null) {
                term = term + line;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return term;
    }

    @FXML
    private void submitButtonClicked(){
        if (submitButton.isDisabled()) {return;}
        mediaView.setVisible(false);
        replayButton.setVisible(false);
        submitButton.setDisable(true);
        if((answerField.getText().equals(_searchTerm))||(answerField.getText().trim().equals(_searchTerm.trim()))){
            numberOfCorrect++;
            attemptNumber = 1;
            _userAnswers.add(answerField.getText());
            correctLabel.setVisible(true);
            player.pause();
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished( event -> {
                correctLabel.setVisible(false);
                submitButton.setDisable(false);
                startQuiz();
            } );
            delay.play();
            answerField.setText("");
            return;

        } else {
            wrongLabel.setVisible(true);
            player.pause();
            if (attemptNumber < 2) {
                attemptNumber++;
                labelWithAnswer.setText("Try Again!");
                labelWithAnswer.setVisible(true);
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> {
                    wrongLabel.setVisible(false);
                    player.seek(Duration.ZERO);
                    player.play();
                    mediaView.setVisible(true);
                    labelWithAnswer.setVisible(false);
                    submitButton.setDisable(false);
                });
                delay.play();
            } else {
                attemptNumber = 1;
                _userAnswers.add(answerField.getText());
                labelWithAnswer.setText("Correct answer was: " + _searchTerm);
                labelWithAnswer.setVisible(true);
                PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
                delay.setOnFinished(event -> {
                    wrongLabel.setVisible(false);
                    labelWithAnswer.setVisible(false);
                    submitButton.setDisable(false);
                    startQuiz();
                });
                delay.play();
            }
            answerField.setText("");
            return;
        }
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
        mediaView.toFront();
        replayButton.setVisible(false);
        player.seek(Duration.ZERO);
        player.play();
    }

    @FXML
    private void keyReleased(){
        String text = answerField.getText();
        boolean isEmpty = (text.isEmpty() || text.trim().isEmpty());
        submitButton.setDisable(isEmpty);
    }

    public void stopVideo() {
        if (player != null) {
            player.pause();
        }
    }
}
