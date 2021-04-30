package ch.heigvd.gen.oe.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class StaticHttpServer {

    private static String BASEDIR /* = "com/rememberjava/http" */;

    private static final int PORT = 8080;

    private HttpServer server;

    StaticHttpServer(String baseDir) {
        BASEDIR = baseDir;
    }

    void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/static", new StaticFileHandler(BASEDIR));

        server.start();
    }

    public void stop() {
        server.stop(0);
    }

}
