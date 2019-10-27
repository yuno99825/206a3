package application.scenes;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * FXML controller class for a generic loading screen.
 * Uses executor services to concurrently execute a specified task.
 * Handles the application logic of this scene.
 */
public class LoadingController {
    @FXML
    private Label label;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button cancelButton;
    private boolean success;
    private Task<Void> task;
    private ExecutorService team = Executors.newFixedThreadPool(1,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

    /**
     * Starts the specified task and sets the text of the label to specified text.
     */
    public void start(String labelText, Task<Void> toDo) {
        success = false;
        label.setText(labelText + ", please wait...");
        task = toDo;
        team.submit(this.task);
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            success = true;
            label.setText("Success!");
            cancelButton.setText("Next");
        });
        task.setOnCancelled(e -> {
            label.setText("Cancelled.");
        });
    }

    /**
     * If the button is for cancellation, cancel the task.
     * Also closes the window containing the loading screen.
     */
    @FXML
    private void buttonClicked() {
        if (cancelButton.getText().equals("Cancel")) {
            if (task != null) {
                task.cancel();
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
            }
        }
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public boolean wasSuccessful() {
        return success;
    }

}
