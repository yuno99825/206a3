package creationtasks;

import javafx.concurrent.Task;

import java.io.IOException;

public class FinalCreationTask extends Task<Void> {
    private double framerate;

    public FinalCreationTask(double framerate) {
        this.framerate = framerate;
    }

    @Override
    protected Void call() throws Exception {
        String createCmd = "cat .temp/images/* | ffmpeg -y -framerate " + framerate + " -f image2pipe -i - -i " +
                " .temp/creation_audio.wav -acodec copy .temp/creation.mkv";
        ProcessBuilder createCmdBuilder = new ProcessBuilder("/bin/bash", "-c", createCmd);
        createCmdBuilder.start();
        return null;
    }
}