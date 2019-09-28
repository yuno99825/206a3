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
        if (isCancelled()) {
            return null;
        }
        String createCmd = "cat .temp/images/* | ffmpeg -y -framerate " + framerate + " -f image2pipe -i - -i " +
                " .temp/creation_audio.wav -vcodec copy .temp/creation.mp4";
//        String createCmd = "ffmpeg -framerate " + framerate +" -loop 1 -pattern_type glob -i '.temp/images/*.jpg' " +
//                "-i .temp/creation_audio.wav -c:v libx264 -c:a aac -b:a 192k -shortest creation.mp4";
        ProcessBuilder createCmdBuilder = new ProcessBuilder("/bin/bash", "-c", createCmd);
        Process process = createCmdBuilder.start();

        return null;
    }
}