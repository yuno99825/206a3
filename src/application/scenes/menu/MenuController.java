package application.scenes.menu;

import application.PrimaryScene;
import application.SceneType;
import application.scenes.quiz.QuizViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MenuController extends PrimaryScene {
    @FXML
    private ListView<String> creationListView;
    @FXML
    private Button playQuizButton;
    private CreationListController creationListController;

    @FXML
    private void initialize() {
        creationListController = new CreationListController(creationListView, this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void openQuizView() throws IOException {
        List<String> creationNames = creationListView.getItems();
        QuizViewController controller = (QuizViewController) setScene(SceneType.QUIZ, stage);
        controller.setCreationsList(creationNames);
        stage.setOnCloseRequest(e -> {
            controller.stopVideo();
        });
    }

    @FXML
    private void newCreation() throws IOException {
        setScene(SceneType.CHUNK_SELECTION, stage);
    }

    public void updateList() {
        if (creationListController.update()) {
            playQuizButton.setDisable(false);
        } else {
            playQuizButton.setDisable(true);
        }
    }
}
