package application.scenes.chunkselection;

import application.Chunk;
import application.PrimaryScene;
import application.SceneType;
import application.scenes.mediaselection.MediaSelectionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * FXML controller class for the scene in which users search for text and create audio chunks.
 * Handles the application logic of this scene including:
 * - Selecting chunks to add to creation
 * - Editing the speed and pitch of chunks
 * - Ordering and deleting chunks
 */
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
    private Button searchHelpButton;
    @FXML
    private Button addHelpButton;
    @FXML
    private Button chunksHelpButton;

    @FXML
    private ListView<Chunk> chunksListView;
    private ObservableList<Chunk> chunksList;

    @FXML
    private void initialize() {
        chunksList = FXCollections.observableArrayList(new ArrayList<Chunk>());
        chunksListView.setItems(chunksList);
        // Set the style of the cells of the list of chunks
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
        setToolTip(searchHelpButton, "Search Wikipedia for something you want to learn about.\nThe information will appear below.\n\n" +
                "위키백과로 귀하가 배우고자 하는 것을 찾아 보십시오.\n그 정보는 여기에 있습니다. ");
        setToolTip(addHelpButton, "Highlight text and add it to your creation as audio.\nYou can also change the speed and pitch of the audio.\n\n" +
                "텍스트를 선택하시고 그것을 영상물에 추가 하세요.\n오디오의 속도와 음높이를 바꿀 수 있습니다.");
        setToolTip(chunksHelpButton, "Audio included in your creation will appear here.\nYou can preview and change the order of the audio.\n\n" +
                "영상물에 필요한음성 파일은 여기에 있습니다.\n미리보기 와 음성 순서 수정이 가능합니다.");
    }

    /**
     * Called when a search is made.
     * Creates a search controller object to concurrently do the wikipedia search.
     */
    @FXML
    private void searchButtonClicked() {
        if (!searchButton.isDisabled()) {
            SearchController searchController = new SearchController(searchField,searchButton,searchResultsArea,searchPrompt);
            searchController.search();
        }
    }

    /**
     * Called when the user attempts to add an audio chunk from selected text.
     */
    @FXML
    private void addButtonClicked() {
        String selectedText = searchResultsArea.getSelectedText();
        int pitch = (int) pitchSlider.getValue();
        double speed = speedSlider.getValue();

        // Selected text should not be empty
        if (!selectedText.isEmpty()) {
            // Selected text shohuld not be over 50 words
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

    /**
     * Removes a selected chunk from inclusion in the creation.
     */
    @FXML
    private void deleteButtonClicked() {
        chunksList.remove(chunksListView.getSelectionModel().getSelectedItem());
        if (chunksList.isEmpty()) {
            nextButton.setDisable(true);
        }
    }

    /**
     * Moves a selected chunk up in the order of included creation audio chunks.
     */
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

    /**
     * Moves a selected chunk down in the order of included creation audio chunks.
     */
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

    /**
     * Plays the audio of a selected chunk.
     */
    @FXML
    private void previewButtonClicked() throws IOException {
        Chunk selectedChunk = chunksListView.getSelectionModel().getSelectedItem();
        PreviewController.preview(selectedChunk);
    }

    /**
     * Creates a new loading screen in a new window which downloads images.
     * If the download is successfully completed, sets the scene of the main window to the media selection screen.
     */
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
