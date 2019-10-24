package application.scenes.chunkselection;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Locale;

public class SearchTask extends Task<String> {
    private String searchTerm;

    public SearchTask(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * Uses a process to run a bash script to search wikipedia for a specified term.
     * Returns the results, assuming the search term was not ambiguous.
     */
    @Override
    protected String call() throws Exception {
        String results = "";
        if (!searchTerm.isEmpty()) {
                String cmd = "wikit " + searchTerm + " | sed 's/  //1'";
                ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
                Process process = builder.start();
                InputStream stdout = process.getInputStream();
                BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
                String line;
                while ((line = stdoutBuffered.readLine()) != null) {
                    results = results + line;
                }
        }
        return separateSentences(results);
    }

    private static String separateSentences(String text) {
        String sentences = "";
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            String sentence = text.substring(start,end);
            sentences = sentences + sentence + "\n";
        }
        return sentences;
    }
}
