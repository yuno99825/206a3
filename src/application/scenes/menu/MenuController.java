package application.scenes.menu;

import application.Main;
import application.PrimaryScene;
import application.SceneType;
import application.scenes.quiz.QuizViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class MenuController extends PrimaryScene {
    @FXML
    private ListView<String> creationListView;
    ObservableList<String> creationList;
    @FXML
    private Button playQuizButton;
    @FXML
    private Button helpButton;

    @FXML
    private void initialize() {
        setUpCreationList();
        creationListView.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String creation, boolean empty) {
                    super.updateItem(creation, empty);
                    if (creation != null && !creation.isEmpty() && !empty) {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/resources/scenes/menu/CreationListCell.fxml"));
                        try {
                            Parent parent = loader.load();
                            CreationListCell creationListCell = loader.getController();
                            creationListCell.setUp(creation, creationList, playQuizButton);
                            setGraphic(parent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        setGraphic(null);
                        setText(null);
                    }
                }
            };
        });;
        setToolTip(helpButton, "The creations you have made will appear here.\nCreations are educational videos with audio and images." +
                "\n\n귀하가 만드신 영상물은 여기에 있습니다.\n파일은 교육적인 음성과 화면이 포함된 비디오 영상입니다.");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void openQuizView() throws IOException {
        List<String> creationNames = creationListView.getItems();
        QuizViewController controller = (QuizViewController) setScene(SceneType.QUIZ, stage);
        controller.setCreations(creationNames);
        stage.setOnCloseRequest(e -> {
            controller.stopVideo();
        });
    }

    @FXML
    private void newCreation() throws IOException {
        setScene(SceneType.CHUNK_SELECTION, stage);
    }

    private void setUpCreationList() {
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
            creationList = FXCollections.observableArrayList(creationArrayList);
            creationList.remove(0);
            creationList.sort(String::compareTo);
            creationListView.setItems(creationList);
        } catch (IOException e) { }
        playQuizButton.setDisable(creationListView.getItems().isEmpty());;
    }
}
