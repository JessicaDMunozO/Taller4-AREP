package edu.escuelaing.arem.ASE.app;

import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServer {

    /**
     * ApiMovie instance to search for information about a given movie name
     */
    private static ApiMovie myAPI = new ApiMovie();

    /**
     * HashMap that stores web services with the route and the web service
     */
    private static HashMap<String, WebService> services = new HashMap<>();

    /**
     * A unique instance of the HttpServer class
     */
    private static HttpServer _instance = new HttpServer();

    /**
     * An String that stores the directory where static files are going to be shown
     */
    private static String filesDirectory;

    /**
     * An String that represents the content type of the response, as default is
     * text/html
     */
    private static String type = "text/html";

    private static HashMap<String, Method> components = new HashMap<>();

    /**
     * Get the unique instance of the HttpServer class
     * 
     * @return the singleton instance of the HttpServer class
     */
    public static HttpServer getInstance() {
        return _instance;
    }

    public void runServer(String[] args) throws IOException, URISyntaxException, ClassNotFoundException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Register a method
        String responseType = args[0];
        Class<?> c = Class.forName(args[1]);

        if (c.isAnnotationPresent(Component.class)) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    System.out.println("Loading class: " + m.getName());
                    components.put(m.getAnnotation(GetMapping.class).value(), m);
                }
            }
        }

        String pathGetCall = "/component/helloname";
        String queryValue = "Jessica";

        if (pathGetCall.startsWith("/component")) {
            String path = pathGetCall.replace("/component", "");
            if (components.containsKey(path)) {
                Method method = components.get(path);
                if (method.getParameterCount() == 1) {
                    System.out.println("Out: " + method.invoke(null, (Object)queryValue));
                } else {
                    System.out.println("Out: " + method.invoke(null));
                }
            }
        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine = "";

            boolean firstLine = true;
            String uriStr = "";
            String movieName = "";

            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            URI fileuri = new URI(uriStr);
            String path = fileuri.getPath();
            System.out.println("Path: " + path);

            // Query
            String query = fileuri.getQuery();
            System.out.println("Query: " + query);
            String param = "";

            if (query != null) {
                String[] queryParam = query.split("=");
                param = queryParam[1];
            }

            if (uriStr.contains("movie?title")) {
                String[] parts = uriStr.split("=");
                movieName = parts[1];
                // set content type of the response
                if (responseType.equals("json")) {
                    setResponseType("application/json");
                }

                printMovieResult(movieName, out);

            } else {
                try {
                    // Defined services
                    if (path.startsWith("/action")) {
                        String webURI = path.replace("/action", "");
                        if (services.containsKey(webURI)) {
                            outputLine = services.get(webURI).handle(param);
                        }
                    } else if (path.startsWith("/files")) {
                        // Static files from other directory
                        setFilesDirectory("target/classes");
                        outputLine = httpClientHtml(path, clientSocket);
                    } else {
                        // Static files from the initial directory
                        setFilesDirectory("target/classes/public");
                        outputLine = httpClientHtml(path, clientSocket);
                    }
                } catch (IOException e) {
                    outputLine = httpError();
                }
            }

            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String httpError() {
        String outputLine = "HTTP/1.1 400 Not Found\r\n"
                + "Content-Type:text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Error Not found</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Error</h1>\n"
                + "    </body>\n";
        return outputLine;

    }

    /**
     * HTTP answer with the content of the specified file on the path
     * 
     * @param path         route to the specified file
     * @param clientSocket socket of the client to send immediately the response, in
     *                     case of an image file
     * @return string with the http answer with the content of the file
     * @throws IOException if there is an error with the inputs or outputs
     */
    public static String httpClientHtml(String path, Socket clientSocket) throws IOException {
        String content_type = "";

        // content type related to the media type of the resource
        if (path.endsWith(".html")) {
            content_type = "text/html";
        } else if (path.endsWith(".js")) {
            content_type = "application/javascript";
        } else if (path.endsWith(".css")) {
            content_type = "text/css";
        } else if (path.endsWith(".png")) {
            content_type = "image/png";
        }

        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type:" + content_type + "\r\n"
                + "\r\n";

        // Read the content of the file from a specific directory
        Path file = Paths.get(filesDirectory + path);
        System.out.println(file);

        Charset charset = Charset.forName("UTF-8");

        if (content_type.equals("image/png")) {
            byte[] imageData = Files.readAllBytes(file);
            OutputStream output = clientSocket.getOutputStream();
            output.write(outputLine.getBytes());
            output.write(imageData);

        } else {
            BufferedReader reader = Files.newBufferedReader(file, charset);
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                outputLine += line;
            }
        }

        return outputLine;

    }

    /**
     * Print information about the specific movie with a specific content type
     * 
     * @param movieName name of the movie to be seached
     * @param out       PrintWriter to send response to the client
     */
    private static void printMovieResult(String movieName, PrintWriter out) {
        String movieInfo;

        try {
            // search for movie information using the API
            movieInfo = myAPI.searchMovieInformation(movieName);
        } catch (Exception e) {
            movieInfo = "Ups, try later.";
            e.printStackTrace();
        }

        String movieResponse = "HTTP/1.1 200 OK\r\n"
                + "Content-Type:" + type + "\r\n"
                + "\r\n"
                + movieInfo;

        out.println(movieResponse);
    }

    /**
     * Register a web service for handling HTTP GET requests with the specified
     * route
     * 
     * @param r route of the web service
     * @param s the web service implementation for handling GET requests
     */
    public static void get(String r, WebService s) {
        services.put(r, s);
    }

    /**
     * Sets the directory from which static files will be served
     * 
     * @param directory the directory path where static files are located
     */
    public static void setFilesDirectory(String directory) {
        filesDirectory = directory;
    }

    /**
     * Sets the response content type
     * 
     * @param responseType the content type that is going to be used to display the
     *                     response
     */
    public static void setResponseType(String responseType) {
        type = responseType;
    }

    /**
     * Register a web service for handling HTTP POST requests with the specified
     * route
     * 
     * @param r route of the web service
     * @param s the web service implementation for handling POST requests
     */
    public static void post(String r, WebService s) {
        services.put(r, s);
    }
}