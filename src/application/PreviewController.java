package application;

import javafx.scene.control.*;

import java.io.*;

public class PreviewController {
    private TextArea searchResultsArea;
    private ToggleGroup voiceToggleGroup;

    public PreviewController(TextArea searchResultsArea, ToggleGroup voiceToggleGroup) {
        this.searchResultsArea = searchResultsArea;
        this.voiceToggleGroup = voiceToggleGroup;
    }

    public void go() throws IOException {
        String text = searchResultsArea.getSelectedText();
        RadioButton voiceButton = (RadioButton) voiceToggleGroup.getSelectedToggle();
        String voice = voiceButton.getText();

        if (!text.isEmpty() || searchResultsArea.getText().isEmpty()) {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "festival");
            Process process = builder.start();
            PrintWriter stdin = new PrintWriter(process.getOutputStream());
            stdin.println(getVoiceName(voice)); // set the voice
            stdin.println("(SayText \"" + text + "\")");
            stdin.close();

        } else {
            // Create warning alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No text selected");
            alert.setHeaderText("Please select some text to preview");
            alert.setContentText("Select text to preview by highlighting it.");
            alert.showAndWait();
        }
    }

    private static String getVoiceName(String description) {
        switch (description) {
            case "NZ Male":
                return "(voice_akl_nz_jdt_diphone)";
            case "NZ Female":
                return "(voice_akl_nz_cw_cg_cg)";
            default:
                return "(voice_kal_diphone)";
        }
    }
}
