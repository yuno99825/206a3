package application;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.io.File;

public class ImageSelectionController {
    @FXML
    private TilePane imagesTilePane;

    public void go() {
        int i = 1;
        for (Node node: imagesTilePane.getChildren()) {
            ImageView imageView = (ImageView) node;
            File file = new File("./.temp/images/" + i + ".jpg");
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            i++;
        }
    }
}
