package com.jasbir.movieapp.Network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Jasbir Singh..
 */

public class NetworkUtils {
    //Make a http connection;
    //send the request
    //get the response and store that in array of MovieDetials objects
    public static String getResponseFromUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
