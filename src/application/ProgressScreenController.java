package application;

import creationtasks.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ProgressScreenController {
    private ObservableList<Chunk> chunks;
    private String searchTerm;
    private int numberOfImages;

    // --- Tasks ---
    private SynthChunksTask synthChunksTask;
    private JoinChunksTask joinChunksTask;
    private GetAudioLengthTask getAudioLengthTask;
    private ImagesTask imagesTask;
    private FinalCreationTask finalCreationTask;

    @FXML
    private Label progressLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button cancelButton;

    private ExecutorService team = Executors.newFixedThreadPool(1,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

    public void setUp (ObservableList<Chunk> chunks, String searchTerm, int numberOfImages) {
        this.chunks = chunks;
        this.searchTerm = searchTerm;
        this.numberOfImages = numberOfImages;
        go();
    }

    private void go() {
        new File(".temp/audio/").mkdirs();

        progressBar.setProgress(0);
        progressLabel.setText("Synthesizing audio chunks...");

        synthChunksTask = new SynthChunksTask(chunks);
        team.submit(synthChunksTask);
        synthChunksTask.setOnSucceeded(e -> {

            progressBar.setProgress(0.2);
            progressLabel.setText("Combining chunks...");
            int numChunks = chunks.size();
            joinChunksTask = new JoinChunksTask(numChunks);
            team.submit(joinChunksTask);
            joinChunksTask.setOnSucceeded(f -> {

                progressBar.setProgress(0.4);
                progressLabel.setText("Getting length of audio...");
                getAudioLengthTask = new GetAudioLengthTask();
                team.submit(getAudioLengthTask);
                getAudioLengthTask.setOnSucceeded(g -> {

                    progressBar.setProgress(0.6);
                    progressLabel.setText("Downloading images...");
                    double length = 0;
                    try {
                        length = getAudioLengthTask.get();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (ExecutionException ex) {
                        ex.printStackTrace();
                    }
                    double framerate = numberOfImages/length;
                    imagesTask = new ImagesTask(searchTerm, numberOfImages);
                    team.submit(imagesTask);
                    imagesTask.setOnSucceeded(h -> {

                        progressBar.setProgress(0.8);
                        progressLabel.setText("Finalising...");
                        finalCreationTask = new FinalCreationTask(framerate);
                        team.submit(finalCreationTask);
                        finalCreationTask.setOnSucceeded(i -> {
                            progressBar.setProgress(1.0);
                            progressLabel.setText("Success!");
                        });
                    });
                });
            });
        });
    }





}
