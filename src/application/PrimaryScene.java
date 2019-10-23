package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class PrimaryScene {
    protected Stage stage;

    public PrimaryScene setScene(SceneType sceneType, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(sceneType.getPath()));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);

        PrimaryScene primaryScene = loader.getController();
        primaryScene.stage = stage;
        return primaryScene;
    }
}
