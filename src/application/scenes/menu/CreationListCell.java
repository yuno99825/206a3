package application.scenes.menu;

import application.Main;
import application.scenes.VideoPlayerController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class CreationListCell {
    @FXML
    private Label creationNameLabel;
    private ObservableList<String> creationList;
    private Button quizButton;

    public void setUp(String text, ObservableList<String> creationList, Button quizButton) {
        creationNameLabel.setText(text);
        this.creationList = creationList;
        this.quizButton = quizButton;
    }

    @FXML
    private void playCreation() throws IOException {
        String creationName = creationNameLabel.getText();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/resources/scenes/VideoPlayer.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent, 500, 500);
        stage.setScene(scene);
        VideoPlayerController controller = loader.getController();
        controller.setUpVideo(creationName, stage);
        stage.setOnCloseRequest(e -> {
            controller.stopVideo();
        });
        stage.show();
    }

    @FXML
    private void deleteCreation() throws IOException, InterruptedException {
        String creationName = creationNameLabel.getText();
        // Create confirmation alert
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to delete " + creationName + "?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Delete");
        alert.setHeight(150);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            String cmd = "rm -fr ./creations/\"" + creationName + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            Process deleteProcess = builder.start();
            deleteProcess.waitFor();
            creationList.remove(creationName);
            System.out.println(creationList);
            quizButton.setDisable(creationList.isEmpty());
        }
    }

}
