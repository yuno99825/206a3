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
 * This Class manages the quiz section of the application and is the controller of the QuizView fxml file.
 * This class is responsible for displaying the quiz videos (Creations), recording user answers and alerting users of
 * their success or failure for each question
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
     * randomizes the order of the Creations for the quiz
     * @param creations
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
     * Displays the Creation in the media view. Each time it is called, this method displays the next question (Creation).
     * Sets the label which tells user which question it is. If they're no more questions left it calls for the
     * statistics of the quiz to be displayed
     *
     * @throws IOException
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
     * Checks if the user's answer is correct, and if so it briefly notifies the user they were
     * correct and then calls the next question. If the user is incorrect it checks whether it is their
     * first attempt or second. If it is their first try it allows for a second try. If it is their second try
     * it tells them the correct answer and continues to the next question.
     */
    @FXML
    private void submitButtonClicked(){
        if (!submitButton.isDisabled()) {
            mediaView.setVisible(false);
            replayButton.setVisible(false);
            submitButton.setDisable(true);
            if ((answerField.getText().equalsIgnoreCase(searchTerm)) ||
                    (answerField.getText().trim().equalsIgnoreCase(searchTerm.trim()))) {
                answerLabel.setText("Correct!");
                numCorrect++;
                attemptNumber = 1;
                correctAnswers.add(searchTerm);
                userAnswers.add(answerField.getText());
                questionNum++;
            } else if (attemptNumber < 2) {
                answerLabel.setText("Incorrect, try again.");
                attemptNumber++;
            } else {
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
     * Changes the scene to that of the quiz statistics screen
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
     * Takes the Creation name for the given question and looks in the text file associated with that
     * Creation. It then retrieves the search term for that Creation and returns it.
     * @param creationName
     * @return the term that was searched for the specific Creation passed in.
     * @throws IOException
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
