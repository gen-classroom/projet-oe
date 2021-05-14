package ch.heigvd.gen.oe.command;

import ch.heigvd.gen.oe.Oe;
import ch.heigvd.gen.oe.server.StaticHttpServer;
import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * Unit tests of command init
 *
 * @author Zwick Gaétan, Tevaearai Rébecca
 */
public class ServeTest {
    private static final String DIR_NAME = "testSite";

    @BeforeAll
    public static void initSite() {
        new CommandLine(new Oe()).execute("init", DIR_NAME);
        new CommandLine(new Oe()).execute("build", DIR_NAME);
    }

    StaticHttpServer server = new StaticHttpServer(DIR_NAME);

    @Test
    public void testDownload() throws IOException {
        server.start();
        URL url = new URL("http://localhost:8080/index.html");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        in.lines().forEach(System.out::println);
        in.close();
        server.stop();
    }

    @Test
    public void testFileNotFound() throws IOException {
        server.start();
        Exception exception = Assertions.assertThrows(FileNotFoundException.class, () -> {
            URL url = new URL("http://localhost:8080/not_found");
            url.openStream();
        });
        server.stop();
    }

    @AfterAll
    public static void cleanSite() {
        System.out.println("clean testSite");

        DFSFileExplorer dfsPost = new DFSFileExplorer(File::delete, false);
        try {
            dfsPost.visit(new File(DIR_NAME));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
