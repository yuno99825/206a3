package application.scenes.chunkselection;

import application.Chunk;
import javafx.scene.control.*;

import java.io.*;

public class PreviewController {

    public static void preview(Chunk chunk) throws IOException {
        if (chunk != null) {
            String text = chunk.getText().replace("\"", "");
            String stretchCommand = chunk.getStretchCommand();
            String pitchCommand = chunk.getPitchCommand();

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
