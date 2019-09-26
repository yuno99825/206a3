package creationtasks;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.*;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImagesTask extends Task<Void> {

    private String searchTerm;
    private int numberOfImages;

    public ImagesTask(String searchTerm, int numberOfImages) {
        this.searchTerm = searchTerm;
        this.numberOfImages = numberOfImages;
    }

    @Override
    protected Void call() {
        {
            try {
                String apiKey = getAPIKey("apiKey");
                String sharedSecret = getAPIKey("sharedSecret");

                Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

                String query = searchTerm;
                int resultsPerPage = numberOfImages;
                int page = 0;

                PhotosInterface photos = flickr.getPhotosInterface();
                SearchParameters params = new SearchParameters();
                params.setSort(SearchParameters.RELEVANCE);
                params.setMedia("photos");
                params.setText(query);

                PhotoList<Photo> results = photos.search(params, resultsPerPage, page);

                for (Photo photo: results) {
                    try {
                        BufferedImage image = photos.getImage(photo, Size.LARGE);
                        String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
                        String pathToImages = ".temp" + System.getProperty("file.separator") + "images";
                        new File(pathToImages).mkdirs();
                        File outputfile = new File(pathToImages,filename);
                        ImageIO.write(image, "jpg", outputfile);
                        System.out.println("Downloaded "+filename);
                    } catch (FlickrException fe) {
                        System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
