package application;

import com.fasterxml.jackson.databind.deser.CreatorProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class CreationToolController {

    @FXML
    private Label searchPrompt;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TextArea searchResultsArea;
    @FXML
    private Slider pitchSlider;
    @FXML
    private Slider speedSlider;
    @FXML
    private Button nextButton;
    @FXML
    private Slider imageSlider;
    @FXML
    private ListView<Chunk> chunksListView;
    private ObservableList<Chunk> chunksList;

    @FXML
    private void initialize() {
        chunksList = FXCollections.observableArrayList(new ArrayList<Chunk>());
        chunksListView.setItems(chunksList);
        chunksListView.setCellFactory(e -> new ListCell<Chunk>(){
            protected void updateItem(Chunk chunk, boolean empty) {
                super.updateItem(chunk, empty);
                if (empty || chunk == null || chunk.getText() == null) {
                    setText(null);
                } else {
                    setText(chunk.getText());
                    setMinWidth(e.getWidth()-10);
                    setMaxWidth(e.getWidth()-10);
                    setPrefWidth(e.getWidth()-10);
                    setWrapText(true);
                }
            }
        });
    }

    @FXML
    private void searchButtonClicked() {
        SearchController searchController = new SearchController(searchField,searchButton,searchResultsArea,searchPrompt);
        searchController.go();
    }

    @FXML
    private void addButtonClicked() {
        String selectedText = searchResultsArea.getSelectedText();
        int pitch = (int) pitchSlider.getValue();
        double speed = speedSlider.getValue();
        if (!selectedText.isEmpty()) {
            if (countWords(selectedText) <= 50) {
                chunksList.add(new Chunk(selectedText, speed, pitch));
                if (searchPrompt.getText().equals("You searched: ")) {
                    nextButton.setDisable(false);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "The selected chunk is too long (max 50 words)", ButtonType.OK);
                alert.setTitle("Chunk too long");
                alert.setHeight(150);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Please select text to add/preview by highlighting.", ButtonType.OK);
            alert.setTitle("No text selected");
            alert.setHeight(150);
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteButtonClicked() {
        chunksList.remove(chunksListView.getSelectionModel().getSelectedItem());
        if (chunksList.isEmpty()) {
            nextButton.setDisable(true);
        }
    }

    @FXML
    private void previewButtonClicked() throws IOException {
        PreviewController previewController = new PreviewController(chunksListView);
        previewController.go();
    }

    @FXML
    private void nextButtonClicked() throws IOException, InterruptedException {
        removeTempFolder();
        String searchTerm = searchField.getText();
        int numberOfImages = (int) imageSlider.getValue();
        ObservableList<Chunk> chunks = chunksListView.getItems();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProgressScreen.fxml"));
        Parent root = loader.load();
        ProgressScreenController controller = loader.getController();
        controller.setUp(chunks, searchTerm, numberOfImages);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        if (controller.isSuccess()) {
            FXMLLoader creationPreviewLoader = new FXMLLoader(getClass().getResource("CreationPreview.fxml"));
            CreationPreviewController creationPreviewController = creationPreviewLoader.getController();
            Parent creationPreviewRoot = creationPreviewLoader.load();
            Stage thisStage = (Stage) nextButton.getScene().getWindow();
            stage.setOnCloseRequest(e -> creationPreviewController.stopVideo());
            thisStage.setScene(new Scene(creationPreviewRoot, 600, 600));
        }
    }

    private void removeTempFolder() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "rm -fr .temp");
        Process process = processBuilder.start();
        process.waitFor();
    }

    private static int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(text);
        return tokens.countTokens();
    }
}
