package application;

import com.fasterxml.jackson.databind.deser.CreatorProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

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
    private ToggleGroup voiceToggleGroup;
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
                    setMinWidth(e.getWidth());
                    setMaxWidth(e.getWidth());
                    setPrefWidth(e.getWidth());
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
        RadioButton selectedVoiceButton = (RadioButton) voiceToggleGroup.getSelectedToggle();
        String voice = selectedVoiceButton.getText();
        if (!selectedText.isEmpty()) {
            chunksList.add(new Chunk(selectedText, voice));
        }
    }

    @FXML
    private void deleteButtonClicked() {
        chunksList.remove(chunksListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void previewButtonClicked() throws IOException {
        PreviewController previewController = new PreviewController(chunksListView);
        previewController.go();
    }

    @FXML
    private void nextButtonClicked() throws IOException, InterruptedException {
        String searchTerm = searchField.getText();
        int numberOfImages = (int) imageSlider.getValue();
        Creator creator = new Creator(chunksListView, searchTerm, numberOfImages);
        creator.makeCreation();
    }
}
