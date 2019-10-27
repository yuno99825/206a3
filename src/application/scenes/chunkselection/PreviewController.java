package application.scenes.chunkselection;

import application.Chunk;

import java.io.*;

/**
 * Class to handle the application logic of previewing an audio chunk in the chunk selection screen.
 */
class PreviewController {

    /**
     * Uses a bash process to preview the audio chunk.
     * @param chunk The chunk to preview
     */
    static void preview(Chunk chunk) throws IOException {
        if (chunk != null) {
            // Need to remove speech marks from the text in order for festival to work
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
