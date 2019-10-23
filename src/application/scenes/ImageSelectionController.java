package application.scenes;

import application.Chunk;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageSelectionController {
    @FXML
    private TilePane imagesTilePane;
    @FXML
    private Button nextButton;
    private String searchTerm;
    private ObservableList<Chunk> chunks;
    private List<Integer> selectedImages = new ArrayList<Integer>();

    @FXML
    private void initialize() {
        int i = 1;
        for (Node node: imagesTilePane.getChildren()) {
            StackPane stackPane = (StackPane) node;
            ImageView imageView = (ImageView) stackPane.getChildren().get(0);
            File file = new File(".temp/images/" + i + ".jpg");
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            i++;
        }
    }

    public void setUp(String searchTerm, ObservableList<Chunk> chunks) {
        this.searchTerm = searchTerm;
        this.chunks = chunks;
    }

    @FXML
    private void imageClicked(Event event) {
        StackPane stackPane = (StackPane) event.getSource();
        VBox vBox = (VBox) stackPane.getChildren().get(1);
        vBox.setVisible(!vBox.isVisible());
    }

    @FXML
    private void nextButtonClicked() throws IOException, InterruptedException {
        int i = 1;
        int numImages = 0;
         for (Node node: imagesTilePane.getChildren()) {
            StackPane stackPane = (StackPane) node;
            VBox vBox = (VBox) stackPane.getChildren().get(1);
            if (vBox.isVisible()) {
                selectedImages.add(i);
                numImages++;
            }
            i++;
        }
        FXMLLoader loader = new FXMLLoader(CreationToolController.class.getResource("/view/ProgressScreen.fxml"));
        Parent root = loader.load();
        ProgressScreenController progressScreenController = loader.getController();
        progressScreenController.go(chunks, searchTerm, selectedImages);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        if (progressScreenController.isSuccess()) {
            FXMLLoader creationPreviewLoader = new FXMLLoader(CreationToolController.class.getResource("/view/CreationPreview.fxml"));
            Parent creationPreviewRoot = creationPreviewLoader.load();
            CreationPreviewController creationPreviewController = creationPreviewLoader.getController();
            Stage thisStage = (Stage) nextButton.getScene().getWindow();
            thisStage.setOnCloseRequest(e -> {
                e.consume();
                try {
                    creationPreviewController.cancelButtonClicked();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            thisStage.setScene(new Scene(creationPreviewRoot, 460, 557));
        } else {
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "rm -fr .temp/images/selected");
            Process process = pb.start();
            process.waitFor();
        }
    }
}
