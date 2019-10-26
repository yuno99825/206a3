package application.scenes;

import application.PrimaryScene;
import application.SceneType;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class QuizStatsController extends PrimaryScene {


    @FXML
    private Label recapMessageLabel;
    @FXML
    private Label accuracyPercentLabel;
    @FXML
    private Label congratulationsLabel;
    @FXML
    private GridPane gridPane;

    private int _numberOfCorrect;
    private int _questionNumber;
    private List<String> _creationsInOrder = new ArrayList<String>();
    private List<String> _userAnswers = new ArrayList<String>();
    private List<Label> _creationsInOrderForGrid = new ArrayList<Label>();
    private List<Label> _userAnswersForGrid = new ArrayList<Label>();

    public void setNumberOfCorrect(int numberOfCorrect) {
        this._numberOfCorrect = numberOfCorrect;
    }
    public void setQuestionNumber(int numberOfQuestions){
        this._questionNumber = numberOfQuestions;
    }
    public void setCreationsInOrder(List<String> creationsInOrder){
        this._creationsInOrder = creationsInOrder;
    }
    public void setUserAnswers(List<String> userAnswers){
        this._userAnswers = userAnswers;
    }

    void setStats(){

        String percent;
        if (_numberOfCorrect == 0){
            congratulationsLabel.setText("You need Practise!");
            percent = "0";
        } else {
            double ratio = (_numberOfCorrect*1.00)/(_questionNumber*1.00);
            ratio = ratio*100.0;
            percent = String.format("%.1f",ratio);
            //percent = Double.toString(ratio);
            if (ratio >= 0.8) {
                congratulationsLabel.setText("Well Done!");
            } else if (ratio >= 0.5) {
                congratulationsLabel.setText("Getting There!");
            } else {
                congratulationsLabel.setText("You need Practise!");
            }
        }
        recapMessageLabel.setText("You got " + Integer.toString(_numberOfCorrect) + " answer(s) correct out of " + Integer.toString(_questionNumber));
        recapMessageLabel.setVisible(true);
        accuracyPercentLabel.setText(percent + "%");
        accuracyPercentLabel.setVisible(true);

        setUpGrid();
    }

    private void setUpGrid(){
        //Color red = Color.RED;
        //Color green = Color.GREEN;

        for (int i = 0; i < _creationsInOrder.size(); i++){

            _creationsInOrderForGrid.add(new Label(_creationsInOrder.get(i)));
            _userAnswersForGrid.add(new Label(_userAnswers.get(i)));
           /** if(((_creationsInOrder.get(i).equals(_userAnswers.get(i))) || _creationsInOrder.get(i).trim().equals(_userAnswers.get(i).trim()))){
                _creationsInOrderForGrid.get(i).setBackground(new Background(new BackgroundFill(green, CornerRadii.EMPTY, Insets.EMPTY)));
                _userAnswersForGrid.get(i).setBackground(new Background(new BackgroundFill(green, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                _creationsInOrderForGrid.get(i).setBackground(new Background(new BackgroundFill(red, CornerRadii.EMPTY, Insets.EMPTY)));
                _userAnswersForGrid.get(i).setBackground(new Background(new BackgroundFill(red, CornerRadii.EMPTY, Insets.EMPTY)));
            }**/
            gridPane.add(_creationsInOrderForGrid.get(i),0,i+1);
            gridPane.add(_userAnswersForGrid.get(i),1,i+1);
        }

    }


    @FXML
    private void backButtonClicked() throws IOException {
        setScene(SceneType.MENU, stage);
    }

}
