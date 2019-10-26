package application;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.*;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class DownloadImagesTask extends Task<Void> {

    private String searchTerm;

    public DownloadImagesTask(String searchTerm) {
        this.searchTerm = searchTerm;
        updateProgress(0, 10);
    }

    @Override
    protected Void call() {
        new File(".temp/images/").mkdirs();
        try {
            String apiKey = getAPIKey("apiKey");
            String sharedSecret = getAPIKey("sharedSecret");
            Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
            String query = searchTerm;
            int page = 0;
            PhotosInterface photos = flickr.getPhotosInterface();
            SearchParameters params = new SearchParameters();
            params.setSort(SearchParameters.RELEVANCE);
            params.setMedia("photos");
            params.setText(query);
            PhotoList<Photo> results = photos.search(params, 10, page);
            int i = 1;
            for (Photo photo: results) {
                if (isCancelled()) {
                    return null;
                }
                try {
                    BufferedImage image = photos.getImage(photo, Size.LARGE);
                    String filename = i + ".jpg";
                    String pathToImages = ".temp" + System.getProperty("file.separator") + "images";
                    File outputfile = new File(pathToImages,filename);
                    ImageIO.write(image, "jpg", outputfile);
                } catch (FlickrException fe) {
                }
                updateProgress(i,10);
                i++;
                if (isCancelled()) {
                    return null;
                }
            }
        } catch (Exception e) {
        }

        return null;
    }

    public static String getAPIKey(String key) throws Exception {
        String config = System.getProperty("user.dir")
                + System.getProperty("file.separator")+ "flickr-api-keys.txt";
        File file = new File(config);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ( (line = br.readLine()) != null ) {
            if (line.trim().startsWith(key)) {
                br.close();
                return line.substring(line.indexOf("=")+1).trim();
            }
        }
        br.close();
        throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
    }
}
