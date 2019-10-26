package application.scenes.chunkselection;

import application.Chunk;
import application.DownloadImagesTask;
import application.PrimaryScene;
import application.SceneType;
import application.scenes.MediaSelectionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ChunkSelectionController extends PrimaryScene {

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
        if (!searchButton.isDisabled()) {
            SearchController searchController = new SearchController(searchField,searchButton,searchResultsArea,searchPrompt);
            searchController.search();
        }
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
                createAlert("Chunk too long", "The selected chunk is too long (max 50 words)");
            }
        } else if (searchResultsArea.isEditable()) {
            createAlert("No text selected", "Please select text to add/preview by highlighting.");
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
    private void moveUpClicked() {
        Chunk selectedChunk = chunksListView.getSelectionModel().getSelectedItem();
        if (selectedChunk != null && chunksList.indexOf(selectedChunk) > 0) {
            int index = chunksList.indexOf(selectedChunk);
            Chunk toSwap = chunksList.get(index - 1);
            chunksList.set(index - 1, selectedChunk);
            chunksList.set(index, toSwap);
            chunksListView.getSelectionModel().select(index - 1);
        }
    }

    @FXML
    private void moveDownClicked() {
        Chunk selectedChunk = chunksListView.getSelectionModel().getSelectedItem();
        if (selectedChunk != null && chunksList.indexOf(selectedChunk) < chunksList.size()-1) {
            int index = chunksList.indexOf(selectedChunk);
            Chunk toSwap = chunksList.get(index + 1);
            chunksList.set(index + 1, selectedChunk);
            chunksList.set(index, toSwap);
            chunksListView.getSelectionModel().select(index + 1);
        }
    }

    @FXML
    private void previewButtonClicked() throws IOException {
        Chunk selectedChunk = chunksListView.getSelectionModel().getSelectedItem();
        PreviewController.preview(selectedChunk);
    }

    @FXML
    private void nextButtonClicked() throws IOException {
        String searchTerm = searchField.getText();
        ObservableList<Chunk> selectedChunks = chunksListView.getItems();
        Task<Void> task = new DownloadImagesTask(searchTerm);

        if (showLoadingScreen("Downloading images", task)) {
            MediaSelectionController mediaSelectionController = (MediaSelectionController) setScene(SceneType.MEDIA_SELECTION, stage);
            mediaSelectionController.setUp(searchTerm, selectedChunks);
        }
    }

    private static int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        StringTokenizer tokens = new StringTokenizer(text);
        return tokens.countTokens();
    }
}
