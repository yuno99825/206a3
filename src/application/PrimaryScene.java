package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class PrimaryScene {
    public static final int APP_WIDTH = 1000;
    public static final int APP_HEIGHT = 750;
    protected Stage stage;

    public PrimaryScene setScene(SceneType sceneType, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(sceneType.getPath()));
        Parent parent = loader.load();
        Scene scene = new Scene(parent, APP_WIDTH, APP_HEIGHT);
        stage.setScene(scene);

        PrimaryScene primaryScene = loader.getController();
        primaryScene.stage = stage;
        return primaryScene;
    }
}
