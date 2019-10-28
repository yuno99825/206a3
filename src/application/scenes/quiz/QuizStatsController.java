package application.scenes.quiz;

import application.PrimaryScene;
import application.SceneType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The FXML controller for the scene in which users see the results of the quiz.
 * Handles the application logic of this scene including setting up and displaying several statistics to the user.
 */
public class QuizStatsController extends PrimaryScene {
    @FXML
    private Label recapMessageLabel;
    @FXML
    private Label accuracyPercentLabel;
    @FXML
    private Label congratulationsLabel;
    @FXML
    private GridPane gridPane;

    private List<String> correctAnswers = new ArrayList<String>();
    private List<String> userAnswers = new ArrayList<String>();
    private int numQuestions;

    /**
     * Compares the correct quiz answers with the answers provided by the user and displays appropriate percentages and congratulations messages.
     * Also displays correct answers next to the users answers in a grid.
     * @param correctAnswers The actual answers for the quiz
     * @param userAnswers What the user answered
     * @param numCorrect The number of questions answered correctly
     */
    public void setStats(List<String> correctAnswers, List<String> userAnswers, int numCorrect) {
        this.correctAnswers = correctAnswers;
        this.userAnswers = userAnswers;
        this.numQuestions = correctAnswers.size();

        double ratio = ((double)numCorrect)/numQuestions * 100;
        String percent = String.format("%.1f",ratio);
        if (ratio >= 0.8) {
            congratulationsLabel.setText("Well Done!");
        } else if (ratio >= 0.5) {
            congratulationsLabel.setText("Getting There!");
        } else {
            congratulationsLabel.setText("You Need Practice!");
        }
        recapMessageLabel.setText("You got " + Integer.toString(numCorrect) + " answer(s) correct out of " + Integer.toString(numQuestions) + ".");
        recapMessageLabel.setVisible(true);
        accuracyPercentLabel.setText(percent + "%");
        accuracyPercentLabel.setVisible(true);

        setUpGrid();
    }

    /**
     * Creates a grid that presents the correct answers along side the answers submitted by the user.
     * It colours correct answers green and incorrect answers red via an external stylesheet.
     */
    private void setUpGrid(){
        for (int i = 0; i < numQuestions; i++){
            HBox correctAnswer = new HBox();
            correctAnswer.getStyleClass().add("stats-h-box");
            correctAnswer.getChildren().add(new Label(correctAnswers.get(i)));
            HBox userAnswer = new HBox();
            userAnswer.getStyleClass().add("stats-h-box");
            userAnswer.getChildren().add(new Label(userAnswers.get(i)));

            if (userAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i)) ||
                    userAnswers.get(i).trim().equalsIgnoreCase(correctAnswers.get(i))) {
                userAnswer.getStyleClass().add("stats-correct");
            } else {
                userAnswer.getStyleClass().add("stats-incorrect");
            }

            gridPane.add(correctAnswer,0,i+1);
            gridPane.add(userAnswer,1,i+1);
        }
    }

    @FXML
    private void backButtonClicked() throws IOException {
        setScene(SceneType.MENU, stage);
    }

}
