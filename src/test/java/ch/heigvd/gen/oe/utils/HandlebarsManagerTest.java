package ch.heigvd.gen.oe.utils;

import ch.heigvd.gen.oe.structure.Config;
import ch.heigvd.gen.oe.structure.Page;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Unit test of HandlebarsManager
 *
 * Gamboni Fiona, Do Vale Lopes Miguel
 */
public class HandlebarsManagerTest {

    private final static String DIRNAME = "tmpTest";

    @BeforeAll
    public static void setUpFakeDir() {
        File dir = new File(DIRNAME);
        dir.mkdir();
    }

    @Test
    public void createShouldCreateCorrectFiles() {
        HandlebarsManager manager = new HandlebarsManager(DIRNAME);
        manager.createTemplate();

        System.out.print("Dir \"templates\" should exist:");
        File file = new File(DIRNAME + "/templates");
        assertTrue(file.exists());
        System.out.println(" yes");

        System.out.print("File \"menu.html\" should exist:");
        file = new File(DIRNAME + "/templates/menu.html");
        assertTrue(file.exists());
        System.out.println(" yes");

        System.out.print("File \"layout.html\" should exist:");
        file = new File(DIRNAME + "/templates/layout.html");
        assertTrue(file.exists());
        System.out.println(" yes");
    }

    @Test
    public void parseMenuShouldWork() {
        final String EXPECTED_MENU = "<ul>\n" +
                "    <li>\n" +
                "        <a href=\"/index.html\">home</a>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <a href=\"/pages/Test1.html\">Test1</a>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <a href=\"/pages/Test2.html\">Test2</a>\n" +
                "    </li>\n" +
                "</ul>";

        HandlebarsManager manager = new HandlebarsManager(DIRNAME);

        // Create pages and config
        Config config = new Config("siteTitle", "site test description",
                "www.websiteTest.com", "EN");
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(new Page("<p>An interesting test content</p>", "2021-04-03", "Test1",
                "F_Tester", "/pages/Test1.html", config));
        pages.add(new Page("<h1>Waoow test</h1>", "2020-04-03", "Test2",
                "Winny_Tester", "/pages/Test2.html", config));

        // Parse the pages and generate menu
        String menu = manager.parseMenu(pages);

        System.out.println("Generated menu:");
        System.out.println(menu);

        assertEquals(EXPECTED_MENU, menu);
    }

    @Test
    public void parsePageShouldWork(){
        final String EXPECTED_PAGE = "<html lang=\"EN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>siteTitle | Test1</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <ul>\n" +
                "    <li>\n" +
                "        <a href=\"/index.html\">home</a>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <a href=\"/pages/Test1.html\">Test1</a>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <a href=\"/pages/Test2.html\">Test2</a>\n" +
                "    </li>\n" +
                "</ul>\n" +
                "    <p>An interesting test content</p>\n" +
                "</body>\n" +
                "</html>";

        HandlebarsManager manager = new HandlebarsManager(DIRNAME);

        // Create pages and config
        Config config = new Config("siteTitle", "site test description",
                "www.websiteTest.com", "EN");
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(new Page("<p>An interesting test content</p>", "2021-04-03", "Test1",
                "F_Tester", "/pages/Test1.html", config));
        pages.add(new Page("<h1>Waoow test</h1>", "2020-04-03", "Test2",
                "Winny_Tester", "/pages/Test2.html", config));

        // Generate menu, set it to page and generate page
        String menu = manager.parseMenu(pages);
        pages.get(0).setMenu(menu);
        String page = manager.parsePage(pages.get(0));

        System.out.println("Generated page:");
        System.out.println(page);

        assertEquals(EXPECTED_PAGE, page);
    }

    @AfterAll
    public static void removeFakeDir() {
        DFSFileExplorer dfsPost = new DFSFileExplorer(File::delete, false);
        try {
            dfsPost.visit(new File(DIRNAME));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}