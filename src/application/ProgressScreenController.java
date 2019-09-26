package application;

import creationtasks.*;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Creator {
    private ObservableList<Chunk> chunks;
    private String searchTerm;
    private int numberOfImages;
    private SynthChunksTask synthChunksTask;
    private GetAudioLengthTask getAudioLengthTask;
    private JoinChunksTask joinChunksTask;
    private ImagesTask imagesTask;
    private FinalCreationTask finalCreationTask;
    private ExecutorService team = Executors.newFixedThreadPool(1,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

    public Creator (ObservableList<Chunk> chunks, String searchTerm, int numberOfImages) {
        this.chunks = chunks;
        this.searchTerm = searchTerm;
        this.numberOfImages = numberOfImages;
    }

    public void makeCreation() throws IOException, InterruptedException, ExecutionException {
        new File(".temp/audio/").mkdirs();

        // Synthesize and save each audio chunk
        synthChunksTask = new SynthChunksTask(chunks);
        team.submit(synthChunksTask);
        synthChunksTask.setOnSucceeded(e -> {

            // Concatenate all audio chunks
            int numChunks = chunks.size();
            joinChunksTask = new JoinChunksTask(numChunks);
            team.submit(joinChunksTask);
            joinChunksTask.setOnSucceeded(f -> {

                // Get length of audio
                getAudioLengthTask = new GetAudioLengthTask();
                team.submit(getAudioLengthTask);
                getAudioLengthTask.setOnSucceeded(g -> {

                    // Download images
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

                        // Create final creation
                        finalCreationTask = new FinalCreationTask(framerate);
                        team.submit(finalCreationTask);
                    });
                });
            });
        });
    }
}
