package application;

import java.util.concurrent.ExecutionException;

public class imagesController {

private String term;
private int numberOfImages;

public imagesController(String term, int numberOfImages){
    this.term = term;
    this.numberOfImages = numberOfImages;
}

public void getImages()  {
    try {
        ImagesTask task = new ImagesTask(term);
        String unfilteredImageUrls = task.call();
        System.out.println(unfilteredImageUrls);
    } catch (InterruptedException ex) {
        ex.printStackTrace();
    } catch (ExecutionException ex) {
        ex.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }

}

}
