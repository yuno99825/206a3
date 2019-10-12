package application;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class ImageSelectionController {
    @FXML
    private TilePane imagesTilePane;

    @FXML
    private void initialize() {
        int i = 1;
        for (Node node: imagesTilePane.getChildren()) {
            ImageView imageView = (ImageView) node;
            imageView.setImage(new Image(".temp/images/" + i + ".jpg"));
        }
    }
}
