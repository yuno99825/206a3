package application.scenes;

import application.Chunk;
import javafx.scene.control.*;

import java.io.*;

public class PreviewController {
    private ListView<Chunk> chunkListView;

    public PreviewController(ListView<Chunk> chunkListView) {
        this.chunkListView = chunkListView;
    }

    public void go() throws IOException {
        Chunk selectedChunk = chunkListView.getSelectionModel().getSelectedItem();
        if (selectedChunk != null) {
            String text = selectedChunk.getText().replace("\"", "");
            String stretchCommand = selectedChunk.getStretchCommand();
            String pitchCommand = selectedChunk.getPitchCommand();

            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "festival");
            Process process = builder.start();
            PrintWriter stdin = new PrintWriter(process.getOutputStream());
            stdin.println(stretchCommand); // set the stretch
            stdin.println(pitchCommand); // set the pitch
            stdin.println("(SayText \"" + text + "\")");
            stdin.close();
        }
    }
}
