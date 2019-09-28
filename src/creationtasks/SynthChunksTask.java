package creationtasks;

import application.Chunk;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.PrintWriter;

public class SynthChunksTask extends Task<Void> {
    private ObservableList<Chunk> chunks;

    public SynthChunksTask (ObservableList<Chunk> chunks) {
        this.chunks = chunks;
    }

    @Override
    protected Void call() throws Exception {
        int i = 1;
        for (Chunk chunk : chunks) {
            if (isCancelled()) {
                return null;
            }
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "festival");
            Process process = builder.start();
            PrintWriter stdin = new PrintWriter(process.getOutputStream());
            String text = chunk.getText().replace("\"", "");
            stdin.println(chunk.getStretchCommand()); // set the stretch
            stdin.println(chunk.getPitchCommand()); // set the pitch
            stdin.println("(set! utt" + i + " (Utterance Text \"" + text + "\"))"); // create utterance
            stdin.println("(utt.save.wave (utt.synth utt" + i + " ) \".temp/audio/" + i + ".wav\" 'riff)"); // synthesize the utterance and save
            stdin.close();
            process.waitFor();
            i++;
        }
        return null;
    }
}
