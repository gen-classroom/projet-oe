package ch.heigvd.gen.oe.server;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Class StaticHttpServer used to create a server
 *
 * @author Zwick Gaétan, Tevaearai Rébecca
 */
public class StaticHttpServer {

    // base directory where the build folder should exist
    private static String BASEDIR;

    // default port 8080
    private static final int PORT = 8080;

    // instance of the http server
    private HttpServer server;

    /**
     * StaticHttpServer constructor
     *
     * @param baseDir base directory of the site
     */
    public StaticHttpServer(String baseDir) {
        BASEDIR = baseDir;
    }

    /**
     * Called to start a new server
     *
     * @return true if the server started without problems otherwise throw an IOException
     * @throws IOException file error
     */
    public boolean start() throws IOException {
        // new http server
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // check if BASEDIR/build exist
        File file = new File(BASEDIR + "/build");
        file.createNewFile();

        // if no build directory exists at BASEDIR
        if (!file.exists())
            throw new FileNotFoundException("The specified directory does not contains a \"build\" folder\nServer not started");

        // add server context
        server.createContext("/", new StaticFileHandler(BASEDIR + "/build"));
        // start the server
        server.start();

        return true;
    }

    /**
     * Called to stop the server
     */
    public void stop() {
        server.stop(0);
    }

}
