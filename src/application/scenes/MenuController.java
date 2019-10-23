package application.scenes;

import application.PrimaryScene;
import application.SceneType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class MenuController extends PrimaryScene {
    @FXML
    private ListView<String> creationListView;
    @FXML
    private Label creationListLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Button playButton;
    @FXML
    private Button playQuizButton;

    @FXML
    private void initialize() {
        updateCreationList();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void deleteCreation() throws IOException {
        String creationName = creationListView.getSelectionModel().getSelectedItem();

        // Create confirmation alert
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete " + creationName + "?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Delete");
        alert.setHeight(150);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            String cmd = "rm -fr ./creations/\"" + creationName + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            Process process = builder.start();
            updateCreationList();
        }
    }

    @FXML
    private void playCreation() throws IOException {
        String creationName = creationListView.getSelectionModel().getSelectedItem();
        VideoPlayerController controller = (VideoPlayerController) setScene(SceneType.VIDEO_PLAYER, stage);
        controller.setUpVideo(creationName, stage);
        stage.setOnCloseRequest(e -> {
            controller.stopVideo();
        });
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
    private void openCreationTool() throws IOException {
        setScene(SceneType.CREATION_TOOL, stage);
    }

    private void updateCreationList() {
        try{
            String cmd = "bash ./scripts/listCreations.sh";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            Process process = builder.start();
            InputStream stdout = process.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

            String output = "";
            String line;
            while ((line = stdoutBuffered.readLine()) != null) {
                output = output + line;
            }

            List<String> creationArrayList = Arrays.asList(output.split("./creations/"));
            ObservableList<String> creationList = FXCollections.observableArrayList(creationArrayList);
            creationList.remove(0);
            creationList.sort(String::compareTo);
            creationListView.setItems(creationList);

            if (creationList.isEmpty()) {
                creationListLabel.setText("You have no creations.");
                deleteButton.setDisable(true);
                playButton.setDisable(true);
                playQuizButton.setDisable(true);

            } else {
                creationListView.getSelectionModel().select(0);
                creationListLabel.setText("Existing creations:");
                deleteButton.setDisable(false);
                playButton.setDisable(false);
                playQuizButton.setDisable(false);
            }
        } catch (IOException e) { }
    }
}
