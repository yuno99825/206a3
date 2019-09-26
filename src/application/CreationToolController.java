package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
    private void searchButtonClicked() {
        SearchController searchController = new SearchController(searchField,searchButton,searchResultsArea,searchPrompt);
        searchController.go();
    }

    @FXML
    private void previewButtonClicked() throws IOException {
        PreviewController previewController = new PreviewController(searchResultsArea, voiceToggleGroup);
        previewController.go();
    }



}
