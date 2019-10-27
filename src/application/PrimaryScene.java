package application;

import application.scenes.LoadingController;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Abstract class to represent the FXML controller of a scene of the application that appears in the main window.
 * Allows these controllers to have a reference to the main window.
 */
public abstract class PrimaryScene {
    static final int APP_WIDTH = 1000;
    static final int APP_HEIGHT = 750;
    protected Stage stage; // The main window of the application

    /**
     * Changes the scene of the main window of the application to a specified type.
     * @param sceneType The type of scene to change to.
     * @param stage The main window of the application.
     * @return The FXML controller of the new scene of the main window.
     */
    protected PrimaryScene setScene(SceneType sceneType, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(sceneType.getPath()));
        Parent parent = loader.load();
        Scene scene = new Scene(parent, APP_WIDTH, APP_HEIGHT);
        stage.setScene(scene);

        PrimaryScene primaryScene = loader.getController();
        primaryScene.stage = stage;
        return primaryScene;
    }

    /**
     * Changes the scene of the main window to the home screen after alerting user of lost progress.
     */
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

    /**
     * Creates a new window showing a loading screen for a long-running task.
     * @param text Explanation of task for user.
     * @param task The task to execute in the background.
     * @return Whether or not the task executed successfully.
     */
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

    /**
     * Sets the tool tip of a help button.
     */
    protected void setToolTip(Button helpButton, String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.getStyleClass().add("korean");
        helpButton.setTooltip(tooltip);
    }
}
