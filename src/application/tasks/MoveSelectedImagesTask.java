package application.tasks;

import javafx.concurrent.Task;

import java.util.List;

public class MoveSelectedImagesTask extends Task<Void> {
    private List<Integer> selectedImages;

    public MoveSelectedImagesTask(List<Integer> selectedImages) {
        this.selectedImages = selectedImages;
    }

    @Override
    protected Void call() throws Exception {
        if (isCancelled()) {
            return null;
        }
        ProcessBuilder pb = new ProcessBuilder();
        for (int i = 1; i <= 10; i++) {
            if (selectedImages.contains(i)) {
                pb.command("/bin/bash", "-c", "cp ./.temp/images/" + i + ".jpg ./.temp/images/selected/" + i + ".jpg");
                Process process = pb.start();
                process.waitFor();
            }
        }
        return null;
    }
}
