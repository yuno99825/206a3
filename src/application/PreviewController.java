package application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class PreviewController {
    private TextArea searchResultsArea;

    public PreviewController(TextArea searchResultsArea) {
        this.searchResultsArea = searchResultsArea;
    }

    public void go() throws IOException {
        String text = searchResultsArea.getSelectedText();
        if (!text.isEmpty() || searchResultsArea.getText().isEmpty()) {
            String cmd = "echo \"" + text + "\" | festival --tts";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            Process process = builder.start();
        } else {
            // Create warning alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No text selected");
            alert.setHeaderText("Please select some text to preview");
            alert.setContentText("Select text to preview by highlighting it.");
            alert.showAndWait();
        }
    }
}
