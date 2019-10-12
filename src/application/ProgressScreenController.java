package application;

import creationtasks.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ProgressScreenController {
    private ObservableList<Chunk> chunks;
    private String searchTerm;
    private List<Integer> selectedImages;
    private int numImages;
    private boolean success = false;

    // --- Tasks ---
    private SynthChunksTask synthChunksTask;
    private JoinChunksTask joinChunksTask;
    private GetAudioLengthTask getAudioLengthTask;
    private MoveSelectedImagesTask moveSelectedImagesTask;
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

    public void go (ObservableList<Chunk> chunks, String searchTerm, List<Integer> selectedImages) throws IOException, InterruptedException {
        this.chunks = chunks;
        this.searchTerm = searchTerm;
        this.selectedImages = selectedImages;
        numImages = selectedImages.size();

        new File(".temp/audio/").mkdirs();
        new File(".temp/images/selected").mkdirs();

        // Synthesize each audio chunk into a wav file, in the folder "audio"
        progressBar.setProgress(0);
        progressLabel.setText("Synthesizing audio chunks...");
        synthChunksTask = new SynthChunksTask(chunks);
        team.submit(synthChunksTask);

        // Once audio chunks are all synthesized, combine them into a single audio file, creation_audio.wav
        synthChunksTask.setOnSucceeded(e -> {
            progressBar.setProgress(0.2);
            progressLabel.setText("Combining chunks...");
            int numChunks = chunks.size();
            joinChunksTask = new JoinChunksTask(numChunks);
            team.submit(joinChunksTask);

            // Get the length of the creation_audio.wav file
            joinChunksTask.setOnSucceeded(f -> {
                progressBar.setProgress(0.4);
                progressLabel.setText("Getting length of audio...");
                getAudioLengthTask = new GetAudioLengthTask();
                team.submit(getAudioLengthTask);

                // Copy selected images into a separate folder, .temp/images/selected
                getAudioLengthTask.setOnSucceeded(g -> {
                    progressBar.setProgress(0.4);
                    progressLabel.setText("Getting selected images...");
                    moveSelectedImagesTask = new MoveSelectedImagesTask(selectedImages);
                    team.submit(moveSelectedImagesTask);

                    // Create a slideshow of images, join this with the audio and text overlay to create the final creation
                    moveSelectedImagesTask.setOnSucceeded(h -> {
                        double framerate = 0;
                        try {
                            framerate = numImages / getAudioLengthTask.get();
                            progressBar.setProgress(0.8);
                            progressLabel.setText("Finalising...");
                            finalCreationTask = new FinalCreationTask(framerate, searchTerm);
                            team.submit(finalCreationTask);

                            // Once finished, allow user to progress to creation preview screen via "done" button
                            finalCreationTask.setOnSucceeded(i -> {
                                progressBar.setProgress(1.0);
                                progressLabel.setText("Success!");
                                cancelButton.setText("Done");
                                success = true;
                            });

                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        } catch (ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                });
            });
        });
    }

    @FXML
    private void buttonClicked() throws IOException, InterruptedException {
        if (cancelButton.getText().equals("Cancel")) {
            if (synthChunksTask != null) {
                synthChunksTask.cancel();
            }
            if (joinChunksTask != null) {
                joinChunksTask.cancel();
            }
            if (getAudioLengthTask != null) {
                getAudioLengthTask.cancel();
            }
            if (finalCreationTask != null) {
                finalCreationTask.cancel();
            }
        }
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public boolean isSuccess() {
        return success;
    }
}
