package creationtasks;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JoinChunksTask extends Task<Void> {
    private int numChunks;

    public JoinChunksTask(int numChunks) {
        this.numChunks = numChunks;
    }

    @Override
    protected Void call() throws Exception {
        if (isCancelled()) {
            return null;
        }
        String cmd = "sox";
        for (int i = 1; i <= numChunks; i++) {
            cmd = cmd.concat(" .temp/audio/" + i + ".wav");
        }
        cmd = cmd.concat(" .temp/creation_audio.wav");
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
        Process process = builder.start();
        process.waitFor();
        return null;
    }
}
