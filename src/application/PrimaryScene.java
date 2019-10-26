package application;

import application.scenes.LoadingController;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
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

    @FXML
    private void homeButtonClicked() throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to return to menu?\nAll progress will be lost!", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Return home");
        alert.setHeight(150);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            setScene(SceneType.MENU, stage);
        }
    }

    protected void createAlert(String title, String alertText) {
        Alert alert = new Alert(Alert.AlertType.NONE, alertText, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeight(150);
        alert.showAndWait();
    }

    protected boolean showLoadingScreen(String text, Task<Void> task) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/scenes/Loading.fxml"));
        Parent loadingScreen = loader.load();
        LoadingController loadingController = loader.getController();
        loadingController.start(text, task);
        Stage loadingStage = new Stage();
        loadingStage.setScene(new Scene(loadingScreen));
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.showAndWait();
        return loadingController.wasSuccessful();
    }
}
