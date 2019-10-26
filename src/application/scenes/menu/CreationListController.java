package application.scenes.menu;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CreationListController {
    private ListView<String> creationListView;
    private MenuController menuController;

    public CreationListController(ListView<String> listView, MenuController menuController) {
        this.creationListView = listView;
        this.menuController = menuController;
        update();
        creationListView.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String creation, boolean empty) {
                    super.updateItem(creation, empty);
                    if (creation != null) {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/resources/scenes/menu/CreationListCell.fxml"));
                        try {
                            Parent parent = loader.load();
                            CreationListCell creationListCell = loader.getController();
                            creationListCell.setUp(creation, menuController);
                            setGraphic(parent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        });
    }

    public boolean update() {
        try {
            String cmd = "bash ./resources/scripts/listCreations.sh";
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
        } catch (IOException e) { }
        return !creationListView.getItems().isEmpty();
    }
}
