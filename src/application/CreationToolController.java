package application;

import javafx.fxml.FXML;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class CreationToolController {
    // Make sure threads are killed upon exiting application by making them daemon
    private ExecutorService team = Executors.newFixedThreadPool(1,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private TextArea searchResultsArea;

    @FXML
    private void doSearch() {
        String searchTerm = searchField.getText();
        SearchTask searchTask = new SearchTask(searchTerm);
        team.submit(searchTask);
        searchTask.setOnSucceeded(e -> {
            try {
                searchResultsArea.setText(searchTask.get());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
    }
}
