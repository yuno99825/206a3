package application;

import javafx.scene.control.*;

import java.io.*;

public class PreviewController {
    private ListView<Chunk> chunkListView;

    public PreviewController(ListView<Chunk> chunkListView) {
        this.chunkListView = chunkListView;
    }

    public void go() throws IOException {
        Chunk selectedChunk = chunkListView.getSelectionModel().getSelectedItem();
        String text = selectedChunk.getText();
        String voiceCommand = selectedChunk.getVoiceCommand();

        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "festival");
        Process process = builder.start();
        PrintWriter stdin = new PrintWriter(process.getOutputStream());
        stdin.println(voiceCommand); // set the voice
        stdin.println("(SayText \"" + text + "\")");
        stdin.close();
    }
}
