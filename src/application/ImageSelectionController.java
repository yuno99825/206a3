package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class ImageSelectionController {
    @FXML
    private TilePane imagesTilePane;

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

    @FXML
    private void imageClicked(Event event) {
        StackPane stackPane = (StackPane) event.getSource();
        VBox vBox = (VBox) stackPane.getChildren().get(1);
        vBox.setVisible(!vBox.isVisible());
    }

    @FXML
    private void nextButtonClicked() {
        int i = 0;
        int numImages = 0;
         for (Node node: imagesTilePane.getChildren()) {
            StackPane stackPane = (StackPane) node;
            VBox vBox = (VBox) stackPane.getChildren().get(1);
            if (vBox.isVisible()) {
                //To complete
            }
            i++;
        }
    }
}
