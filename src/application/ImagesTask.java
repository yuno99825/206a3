package application;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImagesTask extends Task<String> {

    private String term;

    public ImagesTask(String term){
        this.term = term;
    }
    @Override
    protected String call(){

        String url = "https://www.flickr.com/search/?text=" + term;
        String cmd = "wget -q " + url + " -O - | grep 'url(//'";

        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream stdout = process.getInputStream();
        InputStream stderr = process.getErrorStream();

        BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
        String line = null;
        String unfilteredImageUrls = "";
        try{
            while ((line = stdoutBuffered.readLine()) != null ) {

                unfilteredImageUrls = unfilteredImageUrls + line;

            }
            System.out.println(unfilteredImageUrls);
        } catch (Exception e){
            e.printStackTrace();
        }


        List<Integer> beginningIndexes = new ArrayList<Integer>();
        List<Integer> endingIndexes = new ArrayList<Integer>();
        List<String> listOfUrls = new ArrayList<String>();
        List<String> listOfNewUrls = new ArrayList<String>();

        int index = 0;
        while(index != -1){
            index = unfilteredImageUrls.indexOf("url(//", index);
            if (index != -1){
                beginningIndexes.add(index);
                index++;
            }
        }
        index = 0;
        int count = 0;
        while(index != -1) {
            if (count < beginningIndexes.size()) {
                index = unfilteredImageUrls.indexOf(")", beginningIndexes.get(count));
                if (index != -1) {
                    endingIndexes.add(index);
                    count++;
                }
            } else {
                index = -1;
            }
        }
        System.out.println(beginningIndexes.size());
        System.out.println(endingIndexes.size());

        for( int i = 0; i < 10; i++){

            listOfUrls.add(unfilteredImageUrls.substring(beginningIndexes.get(i),endingIndexes.get(i)));
            listOfNewUrls.add(listOfUrls.get(i).replace("url(//","https://"));

        }
        System.out.println(listOfUrls.size());
        System.out.println(listOfUrls.get(2));

        return unfilteredImageUrls;
        //wget -q https://www.flickr.com/search/?text=happy -O - | grep "url(//"
    }
}
