package creationtasks;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GetAudioLengthTask extends Task<Double> {

    @Override
    protected Double call() throws Exception {
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "soxi -D .temp/creation_audio.wav ");
        Process process = builder.start();
        InputStream stdout = process.getInputStream();
        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
        double length = Double.parseDouble(stdoutBuffered.readLine());
        return length;
    }
}
