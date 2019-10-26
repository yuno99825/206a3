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

public class LoadingController {
    @FXML
    private Label label;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button cancelButton;

    private boolean wasSuccessful;
    private Task<Void> task;
    private ExecutorService team = Executors.newFixedThreadPool(1,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

    public void start(String labelText, Task<Void> toDo) {
        wasSuccessful = false;
        label.setText(labelText + ", please wait...");
        task = toDo;
        team.submit(this.task);
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            wasSuccessful = true;
            label.setText("Success!");
            cancelButton.setText("Next");
        });
        task.setOnCancelled(e -> {
            label.setText("Cancelled.");
        });
    }

    @FXML
    private void buttonClicked() {
        if (cancelButton.getText().equals("Cancel")) {
            if (task != null) {
                task.cancel();
            }
        }
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
