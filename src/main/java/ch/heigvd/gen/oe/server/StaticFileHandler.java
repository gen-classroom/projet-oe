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


public class StaticFileHandler implements HttpHandler {

    private final String dir;

    public StaticFileHandler(String dir) {
        this.dir = dir;
    }

    public void handle(HttpExchange ex) throws IOException {
        URI uri = ex.getRequestURI();
        String name = new File(uri.getPath()).getName();
        File path = new File(dir, name);

        Headers h = ex.getResponseHeaders();
        h.add("Content-type", "text/html");

        OutputStream out = ex.getResponseBody();

        if (path.exists()) {
            ex.sendResponseHeaders(200, path.length());
            out.write(Files.readAllBytes(path.toPath()));
        } else {
            System.err.println("File not found: " + path.getAbsolutePath());
            ex.sendResponseHeaders(404, 0);
            out.write("404 file not found.".getBytes(StandardCharsets.UTF_8));
        }
        out.close();

    }
}
