package application.scenes.menu;

import application.Main;
import application.scenes.VideoPlayerController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CreationListCell {
    @FXML
    private Label creationNameLabel;
    private MenuController menuController;

    public void setUp(String text, MenuController menuController) {
        creationNameLabel.setText(text);
        this.menuController = menuController;
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
            menuController.updateList();
        }
    }

}
