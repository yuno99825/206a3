package application;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeCreationTask extends Task<Void> {
    private String searchTerm;
    private ObservableList<Chunk> chunks;
    private List<Integer> selectedImages;
    private ProcessBuilder pb = new ProcessBuilder();
    private List<String> commands = new ArrayList<String>();

    public MakeCreationTask(String searchTerm, ObservableList<Chunk> chunks, List<Integer> selectedImages) {
        this.chunks = chunks;
        this.searchTerm = searchTerm;
        this.selectedImages = selectedImages;
        setUpCmds();
    }

    private void setUpCmds() {
        String joinChunksCmd = "sox";
        for (int i = 1; i <= chunks.size(); i++) {
            joinChunksCmd = joinChunksCmd.concat(" .temp/audio/" + i + ".wav");
        }
        joinChunksCmd = joinChunksCmd.concat(" .temp/creation_audio.wav");
        commands.add(joinChunksCmd);
        commands.addAll(new ArrayList<String> (Arrays.asList(
                "bash ./scripts/createSlideShow.sh",
                "ffmpeg -y -i ./.temp/video.mp4 -i ./.temp/creation_audio.wav -c:v copy -c:a aac -strict experimental ./.temp/quiz.mp4",
                "ffmpeg -i \"./.temp/video.mp4\" -vf \"drawtext=fontfile=./CaviarDreams.ttf:fontsize=100: " +
                        "fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text='" + searchTerm + "'\" ./.temp/video_with_text.mp4",
                "ffmpeg -y -i ./.temp/video_with_text.mp4 -i ./.temp/creation_audio.wav -c:v copy " +
                        "-c:a aac -strict experimental ./.temp/creation_no_music.mp4",
                "ffmpeg -i ./.temp/creation_no_music.mp4 -i ./src/resources/music/ambient.mp3 -c:v copy -filter_complex" +
                        " \"[0:a]aformat=fltp:44100:stereo,apad[0a];[1]aformat=fltp:44100:stereo,volume=1.5[1a];[0a][1a]amerge[a]\" -map 0:v -map \"[a]\" " +
                        "-ac 2 -shortest ./.temp/creation.mp4"
        )));
    }

    private boolean synthChunks() throws InterruptedException, IOException {
        int i = 1;
        for (Chunk chunk : chunks) {
            if (isCancelled()) {
                return false;
            }
            pb.command("/bin/bash", "-c", "festival");
            Process process = pb.start();
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
        return true;
    }

    private void moveSelectedImages() throws IOException, InterruptedException {
        for (int i = 1; i <= 10; i++) {
            if (selectedImages.contains(i)) {
                pb.command("/bin/bash", "-c", "cp ./.temp/images/" + i + ".jpg ./.temp/images/selected/" + i + ".jpg");
                Process process = pb.start();
                process.waitFor();
            }
        }
    }

    @Override
    protected Void call() throws Exception {
        synthChunks();
        moveSelectedImages();
        for (String cmd : commands) {
            pb.command(cmd);
            Process process = pb.start();
            if (isCancelled()) {
                process.destroy();
                return null;
            }
            process.waitFor();
        }
        return null;
    }

}
