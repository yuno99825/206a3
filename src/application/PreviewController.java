package application;

import javafx.scene.control.TextArea;

import java.io.IOException;

public class PreviewController {
    private TextArea searchResultsArea;

    public PreviewController(TextArea searchResultsArea) {
        this.searchResultsArea = searchResultsArea;
    }

    public void go() throws IOException {
        String text = searchResultsArea.getSelectedText();
        if (text.isEmpty()) {
            text = searchResultsArea.getText();
        }
        String cmd = "echo \"" + text + "\" | festival --tts";
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
        Process process = builder.start();
    }
}
