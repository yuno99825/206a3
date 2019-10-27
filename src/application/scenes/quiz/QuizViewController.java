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
import java.util.Collections;
import java.util.List;

/**
 * The FXML controller for the scene in which users play the quiz.
 * Handles the application logic of this scene including:
 * - displaying quiz to user and recording their answers
 * - alerting user of success/failure, recording attempts (max 2) per creation
 */
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
    private Label answerLabel;
    @FXML
    private Button helpButton;

    private List<String> creations;
    private List<String> correctAnswers = new ArrayList<>();
    private List<String> userAnswers = new ArrayList<String>();
    private int questionNum = 1;
    private String searchTerm;
    private int numCorrect = 0;
    private int attemptNumber = 1;

    @FXML
    private void initialize() {
        setToolTip(helpButton, "Guess the word! Make sure to spell it correctly.\n\n" +
                "낱말을 맞춰보세요! 맞춤법이 맞는지 확인하세요.");
    }

    /**
     * randomizes the order of the creations for the quiz
     */
    public void setCreations(List<String> creations) {
        Collections.shuffle(creations);
        this.creations = creations;
    }

    @FXML
    private void startButtonClicked() throws IOException {
        startButton.setVisible(false);
        answerField.setEditable(true);
        startQuestion();
    }

    /**
     * Plays a single question (creation) and sets the question number label to the question number.
     * If there are no more questions left, the quiz stats screen is displayed.
     */
    private void startQuestion() throws IOException {
        if (questionNum > creations.size()) {
            dispStatsScreen();
        } else {
            questionLabel.setText("Question " + questionNum + "!");
            submitButton.setDisable(false);
            mediaView.setVisible(true);
            answerField.setText("");
            answerLabel.setVisible(false);

            String creationName = creations.get(questionNum-1);
            searchTerm = getSearchTerm(creationName);

            File videoURL = new File("./creations/" + creationName + "/quiz.mp4");
            Media video = new Media(videoURL.toURI().toString());
            player = new MediaPlayer(video);
            player.setOnEndOfMedia(() -> {
                replayButton.setVisible(true);
            });
            player.setAutoPlay(true);
            mediaView.setMediaPlayer(player);
        }
    }

    /**
     * Checks if the user's answer is correct and displays the result to the user.
     * If the answer was correct or the user has had 2 attempts, the next question is played.
     */
    @FXML
    private void submitButtonClicked(){
        if (!submitButton.isDisabled()) {
            mediaView.setVisible(false);
            replayButton.setVisible(false);
            submitButton.setDisable(true);
            // answer is correct
            if ((answerField.getText().equalsIgnoreCase(searchTerm)) ||
                    (answerField.getText().trim().equalsIgnoreCase(searchTerm.trim()))) {
                answerLabel.setText("Correct!");
                numCorrect++;
                attemptNumber = 1;
                correctAnswers.add(searchTerm);
                userAnswers.add(answerField.getText());
                questionNum++;
            } else if (attemptNumber < 2) { // answer is incorrect, user has another attempt
                answerLabel.setText("Incorrect, try again.");
                attemptNumber++;
            } else { // answer is incorrect and no more attempts left
                answerLabel.setText("Incorrect.\nThe correct answer was: " + searchTerm + ".");
                attemptNumber = 1;
                correctAnswers.add(searchTerm);
                userAnswers.add(answerField.getText());
                questionNum++;
            }
            answerLabel.setVisible(true);
            player.pause();
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                try {
                    startQuestion();
                } catch (IOException e) {
                }
            });
            delay.play();
        }
    }

    /**
     * Changes the scene of the main window to the quiz statistics screen.
     */
    private void dispStatsScreen(){
        try {
            QuizStatsController controller = (QuizStatsController) setScene(SceneType.QUIZ_STATS, stage);
            controller.setStats(correctAnswers,userAnswers,numCorrect);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the search term for a specified creation by reading the saved text file.
     */
    private String getSearchTerm(String creationName) throws IOException {
        String cmd = "cat ./creations/\"" + creationName + "\"/searchTerm.txt";
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
        Process process = builder.start();
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
