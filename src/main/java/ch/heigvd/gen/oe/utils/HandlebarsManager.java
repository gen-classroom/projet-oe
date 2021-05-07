package ch.heigvd.gen.oe.utils;

import ch.heigvd.gen.oe.structure.Page;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Class to manage Handlebars
 * <p>
 * authors: Do Vale Lopes Miguel, Gamboni Fiona
 */
public class HandlebarsManager {

    private static final String DIRNAME_TEMPLATE = "templates";
    private static final String EXTENSION_TYPE = ".html";
    private static final String FILENAME_MENU = "menu";
    private static final String FILENAME_LAYOUT = "layout";

    private static final String TEMPLATE_MENU = "<ul>\n" +
            "    <li>\n" +
            "        <a href=\"/index.html\">home</a>\n" +
            "    <li>\n" +
            "    {{#each}}\n" +
            "    <li>\n" +
            "        <a href=\"/pages/{{{filename}}}\">{{{title}}}</a>\n" +
            "    </li>\n" +
            "    {{/each}}\n" +
            "</ul>";

    private static final String TEMPLATE_LAYOUT = "<html lang=\"{{site.language}}\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>{{site.title}} | {{title}}</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    {{{menu}}}\n" +
            "    {{{content}}}\n" +
            "</body>\n" +
            "</html>";

    private final String dirSiteName;

    /**
     * Constructor
     *
     * @param dirSiteName path to the site directory
     */
    public HandlebarsManager(String dirSiteName) {
        this.dirSiteName = dirSiteName;
    }


    /**
     * Create template directory containing files FILENAME_MENU and FILENAME_LAYOUT
     */
    public void createTemplate() {
        String templatePath = dirSiteName + "/" + DIRNAME_TEMPLATE;

        // Create template dir
        File dirTemplate = new File(templatePath);
        if (dirTemplate.mkdir()) {
            System.out.println("Directory " + DIRNAME_TEMPLATE + " has been created");
        } else {
            System.out.println("Directory " + DIRNAME_TEMPLATE + " already exists and will be overwritten");
        }

        // Create menu.html
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(templatePath + "/" + FILENAME_MENU + EXTENSION_TYPE),
                StandardCharsets.UTF_8))) {

            writer.print(TEMPLATE_MENU);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create layout.html
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(templatePath + "/" + FILENAME_LAYOUT + EXTENSION_TYPE),
                StandardCharsets.UTF_8))) {

            writer.print(TEMPLATE_LAYOUT);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse Pages and return a string menu in html corresponding to TEMPLATE_MENU
     *
     * @param pages a list of Page to parse
     * @return a string of the html menu
     */
    public String parseMenu(List<Page> pages) {
        return parse(pages, FILENAME_MENU);
    }

    /**
     * Parse a Page and return a string in html corresponding to TEMPLATE_PAGE
     *
     * @param page a Page to parse
     * @return a string of the html page
     */
    public String parsePage(Page page) {
        return parse(page, FILENAME_LAYOUT);
    }

    /**
     * Parse an Object and return a string in html corresponding to templateFilename
     *
     * @param o                the Object to parse
     * @param templateFilename a String corresponding to the Template to use
     * @return a string of the html object
     */
    private String parse(Object o, String templateFilename) {
        // Create handlebars parser
        TemplateLoader loader = new FileTemplateLoader(dirSiteName + "/" + DIRNAME_TEMPLATE, EXTENSION_TYPE);
        Handlebars handlebars = new Handlebars(loader);
        handlebars.setPrettyPrint(true);

        // Parse page
        try {
            Template templatePage = handlebars.compile(templateFilename);
            return templatePage.apply(o);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
