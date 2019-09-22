package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController {
    @FXML
    private ListView<String> creationListView;
    @FXML
    private Label creationListLabel;

    @FXML
    private void initialize() {
        updateCreationList();
    }

    @FXML
    private void deleteCreation() throws IOException {
        String creationName = creationListView.getSelectionModel().getSelectedItem();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VideoPlayer.fxml"));
        Parent root = loader.load();

        VideoPlayerController controller = loader.getController();
        controller.setUpVideo(creationName);


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
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
            } else {
                creationListView.getSelectionModel().select(0);
                creationListLabel.setText("Existing creations:");
            }
        } catch (IOException e) { }
    }
}
