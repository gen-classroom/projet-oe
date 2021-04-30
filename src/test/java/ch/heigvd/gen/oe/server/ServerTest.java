package ch.heigvd.gen.oe.server;

import ch.heigvd.gen.oe.Oe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Unit tests of command init
 *
 * @author Zwick Gaétan, Tevaearai Rébecca
 */
public class ServerTest {
    private static final String DIR_NAME = "testSite";

    @BeforeAll
    public static void initSite() {

        new CommandLine(new Oe()).execute("init", DIR_NAME);
        new CommandLine(new Oe()).execute("build", DIR_NAME);

    }

    StaticHttpServer server = new StaticHttpServer(DIR_NAME);

//    @Test
//    public void testPersistent() throws IOException {
//        server.start();
//        while (true);
//    }

    @Test
    public void testDownload() throws IOException {
        server.start();

        URL url = new URL("http://localhost:8080/static/index.html");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        in.lines().forEach(System.out::println);
        in.close();
    }

    @Test
    public void testFilenNotFound() throws IOException {
            server.start();
        Exception exception = Assertions.assertThrows(FileNotFoundException.class, () -> {
            URL url = new URL("http://localhost:8080/static/not_found");
            url.openStream();
        });
    }
}
