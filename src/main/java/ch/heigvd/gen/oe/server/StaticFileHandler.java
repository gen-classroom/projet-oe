package ch.heigvd.gen.oe.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Class StaticFileHandler used to handle the incoming http requests
 *
 * authors: Zwick Gaétan, Tevaearai Rébecca
 */
public class StaticFileHandler implements HttpHandler {

    // base directory of files
    private final String dir;

    public StaticFileHandler(String dir) {
        this.dir = dir;
    }

    /**
     * Called for each incoming http requests
     *
     * @param ex encapsulation of an http request recieved by the server
     */
    public void handle(HttpExchange ex) throws IOException {
        URI uri = ex.getRequestURI();
        File path = new File(dir + "/"  + uri);

        Headers h = ex.getResponseHeaders();
        h.add("Content-type", "text/html");

        OutputStream out = ex.getResponseBody();

        if (path.exists()) { // if the specified path exists
            ex.sendResponseHeaders(200, path.length());
            out.write(Files.readAllBytes(path.toPath()));
        } else { // if the specified path does not exists
            System.err.println("File not found: " + path);
            ex.sendResponseHeaders(404, 0); // send http error 404
            out.write("404 file not found.".getBytes(StandardCharsets.UTF_8)); // send http error 404 page
        }
        out.close();
    }
}
