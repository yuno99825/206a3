package application.scenes;

import creationtasks.ImagesTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DownloadingImagesController {
    @FXML
    private Label progressLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button cancelButton;

    private String searchTerm;
    private boolean success = false;
    private ImagesTask imagesTask;

    private ExecutorService team = Executors.newFixedThreadPool(1,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

    public void go (String searchTerm) {
        progressBar.setProgress(0);
        this.searchTerm = searchTerm;
        imagesTask = new ImagesTask(searchTerm);
        team.submit(imagesTask);
        progressBar.progressProperty().bind(imagesTask.progressProperty());

        imagesTask.setOnSucceeded(e -> {
            progressLabel.setText("Success!");
            cancelButton.setText("Done");
            success = true;
        });
    }

    @FXML
    private void buttonClicked() throws IOException, InterruptedException {
        if (cancelButton.getText().equals("Cancel")) {
            if (imagesTask != null) {
                imagesTask.cancel();
            }
        }
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public boolean isSuccess() {
        return success;
    }

}
