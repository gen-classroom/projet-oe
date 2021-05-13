package ch.heigvd.gen.oe.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Class StaticHttpServer, main class for the http server
 *
 * @authors: Zwick Gaétan, Tevaearai Rébecca
 */
public class StaticHttpServer {

    private static String BASEDIR;

    private static final int PORT = 8080;

    private HttpServer server;

    public StaticHttpServer(String baseDir) {
        BASEDIR = baseDir;
    }

    public boolean start() throws IOException {
        // new http server
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // check if BASEDIR/build exist
        File file = new File(BASEDIR + "/build");
        file.createNewFile();
        if (!file.exists())
            throw new FileNotFoundException("The specified directory does not contains a \"build\" folder\nServer not started");

        // add server context
        server.createContext("/", new StaticFileHandler(BASEDIR + "/build"));
        // start the server
        server.start();

        return true;
    }

    public void stop() {
        server.stop(0);
    }

}
