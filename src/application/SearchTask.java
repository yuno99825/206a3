package application;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        return results;
    }
}
