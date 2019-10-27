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
 * This Class manages the statistics component of the quiz and is the controller for the QuizStats fxml file.
 * This Class is responsible for displaying the users statistics for a single quiz. This occurs directly after
 * a quiz takes place.
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
     * This method compares the correct quiz answers with the answers provided by the user and displays appropriate
     * percentages and congratulations messages. It also calls for the correct answers to be displayed next to the
     * users answers in a grid.
     * @param correctAnswers
     * @param userAnswers
     * @param numCorrect
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
     * This method creates a table that presents the correct answers along side the answers submitted by
     * the user. It colours correct answers green and incorrect answers red.
     */
    private void setUpGrid(){
        for (int i = 0; i < numQuestions; i++){
            HBox correctAnswer = new HBox();
            correctAnswer.getStyleClass().add("stats-h-box");
            correctAnswer.getChildren().add(new Label(correctAnswers.get(i)));
            HBox userAnswer = new HBox();
            userAnswer.getStyleClass().add("stats-h-box");
            userAnswer.getChildren().add(new Label(userAnswers.get(i)));

            if (userAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i))) {
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
