package application.scenes.mediaselection;

import application.Chunk;
import application.PrimaryScene;
import application.SceneType;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FXML controller class for the scene in which user selects which media (images and music) to include in creation.
 * Handles the application logic of this scene.
 */
public class MediaSelectionController extends PrimaryScene {
    @FXML
    private TilePane imagesTilePane;
    @FXML
    private Button nextButton;
    @FXML
    private ComboBox<String> musicComboBox;
    @FXML
    private Button imgsHelpButton;
    @FXML
    private Button musicHelpButton;
    private String searchTerm;
    private ObservableList<Chunk> chunks;
    private List<Integer> selectedImages = new ArrayList<Integer>();
    int numImages = 0;

    @FXML
    private void initialize() {
        int i = 1;
        // Set the images to display in the tile pane
        for (Node node: imagesTilePane.getChildren()) {
            StackPane stackPane = (StackPane) node;
            ImageView imageView = (ImageView) stackPane.getChildren().get(0);
            File file = new File(".temp/images/" + i + ".jpg");
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            i++;
        }
        // Set up the music combo box
        musicComboBox.getItems().addAll(
                "No music",
                "Ambient",
                "Lofi",
                "Downbeat"
        );
        musicComboBox.getSelectionModel().selectFirst();
        setToolTip(imgsHelpButton, "Select at least 1 image to include in your creation.\n\n" +
                "영상물에 삽입할 이미지를 한가지 이상 선택하세요.");
        setToolTip(musicHelpButton, "You can choose to include music in your creation.\n\n" +
                "영상물에 삽입할 음악을 선택할 수 있습니다.");
    }

    public void setUp(String searchTerm, ObservableList<Chunk> chunks) {
        this.searchTerm = searchTerm;
        this.chunks = chunks;
    }

    /**
     * When an image is clicked, toggle whether it is selected or not.
     */
    @FXML
    private void imageClicked(Event event) {
        StackPane stackPane = (StackPane) event.getSource();
        VBox vBox = (VBox) stackPane.getChildren().get(1);
        if (vBox.isVisible()) {
            numImages--;
        } else {
            numImages++;
        }
        vBox.setVisible(!vBox.isVisible());
        if (numImages <= 0) {
            nextButton.setDisable(true);
        } else {
            nextButton.setDisable(false);
        }
    }

    /**
     * Called when the user clicks the next button.
     * Create a new window with a loading screen, concurrently creating the creation from paramters.
     * If creation is successful, change the scene of the main window to the creation preview screen.
     */
    @FXML
    private void nextButtonClicked() throws IOException {
        if (!nextButton.isDisabled()) {
            int i = 1;
            for (Node node: imagesTilePane.getChildren()) {
                StackPane stackPane = (StackPane) node;
                VBox vBox = (VBox) stackPane.getChildren().get(1);
                if (vBox.isVisible()) {
                    selectedImages.add(i);
                }
                i++;
            }
            String pathToMusic = "\"./resources/music/" + musicComboBox.getValue().toLowerCase() + ".mp3\"";
            Task<Void> task = new MakeCreationTask(searchTerm, chunks, selectedImages, pathToMusic);
            if (showLoadingScreen("Creating creation", task)) {
                setScene(SceneType.CREATION_PREVIEW, stage);
            }
        }
    }
}
