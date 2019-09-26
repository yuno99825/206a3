package application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SearchController {
    private TextField searchField;
    private Button searchButton;
    private TextArea searchResultsArea;
    private Label prompt;
    // Make sure threads are killed upon exiting application by making them daemon
    private ExecutorService team = Executors.newFixedThreadPool(1,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });
    private SearchTask searchTask;

    public SearchController(TextField searchField, Button searchButton, TextArea searchResultsArea, Label prompt) {
        this.searchField = searchField;
        this.searchButton = searchButton;
        this.searchResultsArea = searchResultsArea;
        this.prompt = prompt;
    }

    public void go() {
        String searchTerm = searchField.getText();
        if (validateSearchTerm(searchTerm)) {
            searchTask = new SearchTask(searchTerm);
            prompt.setText("Searching...");
            searchField.setEditable(false);
            searchButton.setDisable(true);

            searchTask.setOnSucceeded(e -> {
                try {
                    String searchResults = searchTask.get();
                    if (searchResults.equals(searchTerm + " not found :^(\n")) {
                        searchResultsArea.clear();
                        prompt.setText("Invalid, please enter a search term:");
                        searchField.setEditable(true);
                        searchButton.setDisable(false);
                    }
                    else {
                        searchResultsArea.setText(searchResults);
                        prompt.setText("You searched: ");
                    }

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
            });

            team.submit(searchTask);

        } else {
            prompt.setText("Invalid, please enter a search term:");
        }
    }

    private static boolean validateSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return false;
        }
        return searchTerm.matches("[a-zA-Z0-9 _-]+");
    }

}
