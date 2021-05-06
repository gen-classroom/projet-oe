package ch.heigvd.gen.oe.utils;

import ch.heigvd.gen.oe.structure.Page;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HandlebarsManager {

    private static final String DIRNAME_TEMPLATE = "templates";
    private static final String EXTENSION_TYPE = ".html";
    private static final String FILENAME_MENU = "menu";
    private static final String FILENAME_LAYOUT = "layout";

    private final String dirSiteName;
    private static String menu = "";

    private static final String TEMPLATE_MENU = "<ul>\n" +
            "    <li>\n" +
            "        <a href=\"/index,html\">home</a>\n" +
            "    <li>\n" +
            "    {{#each}}\n" +
            "    <li>\n" +
            "        <a href=\"/pages/{{{title}}}.html\">{{{title}}}</a>\n" +
            "    </li>\n" +
            "    {{/each}}\n" +
            "</ul>";

    private static final String TEMPLATE_LAYOUT = "<html lang=\"{{site.language}}\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>{{site.title}} | {{title}}</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    " + menu + "\n" +
            "    {{{content}}}\n" +
            "</body>\n" +
            "</html>";


    public HandlebarsManager(String dirSiteName) {
        this.dirSiteName = dirSiteName;
    }


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


}
