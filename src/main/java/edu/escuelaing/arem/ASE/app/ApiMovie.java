package edu.escuelaing.arem.ASE.app;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * The ApiMovie class was build to search information about movies
 * Here we use the OMDb API to obtain movie's information
 */
public class ApiMovie {

    /**
     * moviesCache stores movies that have already been searched
     */
    private HashMap<String, StringBuffer> moviesCache = new HashMap<>();

    /**
     * HTTP connection for making queries to the OMDb API
     */
    private HttpConnection connection = new HttpConnection();

    /**
     * Searches information about a particular movie using the OMDb API
     * 
     * @param movieName name of the movie to be searched
     * @return string with the movie information
     * @throws IOException if there is an error with the URL
     */
    public String searchMovieInformation(String movieName) throws IOException {
        String info;
        String name = movieName.toLowerCase();

        String movieURL = "http://www.omdbapi.com/?i=tt3896198&apikey=e6058b22&t=" + movieName;
        URL url = new URL(movieURL);

        if (!moviesCache.containsKey(name)) {
            StringBuffer movieInfo = connection.URLConnection(url);
            moviesCache.put(name, movieInfo);
            info = movieInfo.toString();

        } else {
            info = moviesCache.get(name).toString();
        }

        return info;
    }
}
