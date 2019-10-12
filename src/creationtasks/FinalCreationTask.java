package creationtasks;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FinalCreationTask extends Task<Void> {
    private double framerate;
    private String searchTerm;

    public FinalCreationTask(double framerate, String searchTerm) {
        this.framerate = framerate;
        this.searchTerm = searchTerm;
    }

    @Override
    protected Void call() throws Exception {
        if (isCancelled()) {
            return null;
        }
        ProcessBuilder pb = new ProcessBuilder();
//        pb.command("/bin/bash", "-c", "ffmpeg -f image2 -r " + framerate + " -i './.temp/images/%1d.jpg' -r 30 -y ./.temp/video.mp4");
        pb.command("bash", "-c", "cat ./.temp/images/selected/*.jpg | ffmpeg -f image2pipe -framerate "+framerate+
                " -i - -c:v libx264 -pix_fmt yuv420p -vf \"scale=w=1080:h=720:force_original_aspect_ratio=1,pad=1080:720:(ow-iw)/2:(oh-ih)/2\" -r 25 -y ./.temp/video.mp4");
        Process makeVid = pb.start();

        if (isCancelled()) {
            makeVid.destroy();
            return null;
        }
        makeVid.waitFor();


        pb.command("/bin/bash", "-c", "echo '" + searchTerm + "' > ./.temp/searchTerm.txt");
        Process createTextFile = pb.start();
        if (isCancelled()) {
            createTextFile.destroy();
        }
        createTextFile.waitFor();



        pb.command("/bin/bash", "-c", "ffmpeg -i \"./.temp/video.mp4\" -vf \"drawtext=fontfile=./CaviarDreams.ttf:fontsize=100: " +
                "fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='" + searchTerm + "'\" ./.temp/video_with_text.mp4");
        Process addText = pb.start();
        if (isCancelled()) {
            addText.destroy();
        }
        addText.waitFor();

        pb.command("/bin/bash", "-c", "ffmpeg -y -i ./.temp/video_with_text.mp4 -i ./.temp/creation_audio.wav -c:v copy -c:a aac -strict experimental ./.temp/creation.mp4");
        Process createFinal = pb.start();
        if (isCancelled()) {
            createFinal.destroy();
        }
        createFinal.waitFor();

        return null;
    }
}