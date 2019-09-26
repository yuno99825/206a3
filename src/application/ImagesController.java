package application;

import java.util.concurrent.ExecutionException;

public class ImagesController {

private String term;
private int numberOfImages;

public ImagesController(String term, int numberOfImages){
    this.term = term;
    this.numberOfImages = numberOfImages;
}

public void getImages()  {
    try {
        ImagesTask task = new ImagesTask(term,numberOfImages);
        task.call();
    }  catch (Exception e) {
        e.printStackTrace();
    }

}

}
