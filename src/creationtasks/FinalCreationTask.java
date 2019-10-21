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
            return null;
        }
        createTextFile.waitFor();

        pb.command("/bin/bash", "-c", "ffmpeg -y -i ./.temp/video.mp4 -i ./.temp/creation_audio.wav -c:v copy -c:a aac -strict experimental ./.temp/quiz.mp4");
        Process createQuiz = pb.start();
        if (isCancelled()) {
            createQuiz.destroy();
            return null;
        }
        createQuiz.waitFor();

        pb.command("/bin/bash", "-c", "ffmpeg -i \"./.temp/video.mp4\" -vf \"drawtext=fontfile=./CaviarDreams.ttf:fontsize=100: " +
                "fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='" + searchTerm + "'\" ./.temp/video_with_text.mp4");
        Process addText = pb.start();
        if (isCancelled()) {
            addText.destroy();
            return null;
        }
        addText.waitFor();

        pb.command("/bin/bash", "-c", "ffmpeg -y -i ./.temp/video_with_text.mp4 -i ./.temp/creation_audio.wav -c:v copy " +
                "-c:a aac -strict experimental ./.temp/creation_no_music.mp4");
        Process creationNoMusic= pb.start();
        if (isCancelled()) {
            creationNoMusic.destroy();
            return null;
        }
        creationNoMusic.waitFor();

        pb.command("/bin/bash", "-c", "ffmpeg -i ./.temp/creation_no_music.mp4 -i ./src/resources/music/ambient.mp3 -c:v copy -filter_complex" +
                " \"[0:a]aformat=fltp:44100:stereo,apad[0a];[1]aformat=fltp:44100:stereo,volume=1.5[1a];[0a][1a]amerge[a]\" -map 0:v -map \"[a]\" " +
                "-ac 2 -shortest ./.temp/creation.mp4");
        Process addMusic = pb.start();
        if (isCancelled()) {
            addMusic.destroy();
            return null;
        }
        addMusic.waitFor();

        return null;
    }
}