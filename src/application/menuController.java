package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class menuController {
    @FXML
    private ListView<String> creationListView;
    @FXML
    private Label creationListLabel;

    @FXML
    private void initialize() {
        updateCreationList();
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
