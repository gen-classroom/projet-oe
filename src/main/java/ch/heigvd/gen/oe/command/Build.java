package ch.heigvd.gen.oe.command;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ch.heigvd.gen.oe.structure.Config;
import ch.heigvd.gen.oe.structure.Page;
import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import ch.heigvd.gen.oe.utils.HandlebarsManager;
import ch.heigvd.gen.oe.utils.Json;
import ch.heigvd.gen.oe.utils.Markdown;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Subcommand build
 *
 * author: Miguel Do Vale Lopes, Gamboni Fiona
 */
@Command(name = "build", description = "Build a static site")
public class Build implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    /**
     * Call subcommand build to build the initialised site
     *
     * @return 0
     */
    @Override
    public Integer call() {
        // Create build dir
        File build = new File(dirSiteName + "/build");
        if (build.mkdir()) {
            System.out.println("The directory " + build.getName() + " has been created!");
        } else {
            System.out.println("The directory " + build.getName() + " already exists, files will be overwritten");
        }

        // Get site config
        Config config = new Json().parse(dirSiteName + "/config.json", Config.class);

        // Init a list
        final List<Page> pages = new ArrayList<>();

        // Treat pages directory
        DFSFileExplorer dfsPre = new DFSFileExplorer((File file) -> {

            String path = file.getPath();
            String newPath = path.substring(0, dirSiteName.length())
                    + "/build" + path.substring(dirSiteName.length());

            // File is a dir -> create dir
            if (file.isDirectory()) {
                File dir = new File(newPath);
                if (dir.mkdir()) {
                    System.out.println("The directory " + dir.getName() + " has been created!");
                } else {
                    System.out.println("The directory " + dir.getName() + " already exists, files will be overwritten");
                }
            }

            // File is a markdown file -> create Page
            else if (file.getName().endsWith(".md")) {
                pages.add(createPage(path, config));
            }

            // Copy file
            else {
                copy(path, newPath);
            }

        }, true);

        try {
            dfsPre.visit(new File(dirSiteName + "/pages"));

            // Generate menu
            HandlebarsManager hbManager = new HandlebarsManager(dirSiteName);
            String menu = hbManager.parseMenu(pages);

            // Treat index.md
            Page index = createPage(dirSiteName + "/index.md", config);
            if (index == null) {
                System.err.println("Could not convert index.md to .html");
                return 1;
            }
            index.setMenu(menu);
            convertToHtml(dirSiteName + "/index.md", dirSiteName + "/build/index.md");
            writeToHtmlFile(hbManager.parsePage(index), dirSiteName + "/build/" + index.getFilename());


            // Create html pages
            for (Page page : pages) {
                page.setMenu(menu);
                writeToHtmlFile(hbManager.parsePage(page), dirSiteName + "/build/" + page.getFilename());
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println();
        return 0;
    }

    private void writeToHtmlFile(String html, String dstPathName) {
        try (PrintWriter os = new PrintWriter(new OutputStreamWriter(
                     new FileOutputStream(dstPathName), StandardCharsets.UTF_8))) {

            // Create and write page
            os.print(html);
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Page createPage(String srcPathName, Config config) {

        try (BufferedReader is = new BufferedReader(new InputStreamReader(
                new FileInputStream(srcPathName), StandardCharsets.UTF_8))) {

            // Read file
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = is.readLine()) != null) {
                data.append(line).append("\n");
            }

            // Get metadata
            Markdown markdown = new Markdown();
            String[] mdData = markdown.split(data.toString());
            String[] metadata = parseMetadata(mdData[0]);

            // Get content in html
            String html = markdown.toHtml(mdData[1]);

            // Create page
            String filename = srcPathName.substring(dirSiteName.length()).split(".md$")[0] + ".html";
            return new Page(html, metadata[0], metadata[1], metadata[2], filename, config);

        } catch (FileNotFoundException e) {
            System.err.println(srcPathName + " was not found, please check that you \"init\" before \"build\"");
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO comment
    private String[] parseMetadata(String metadata) {
        Object[] tmp = metadata.lines().toArray();
        String[] result = new String[tmp.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = ((String)tmp[i]).split(":", 2)[1];
        }
        return result;
    }

    /**
     * Copy file in srcPathName to dstPathName
     *
     * @param srcPathName the path of the source file to copy
     * @param dstPathName the destination path to copy the src file
     */
    private void copy(String srcPathName, String dstPathName) {

        try (BufferedReader is = new BufferedReader(new InputStreamReader(
                new FileInputStream(srcPathName), StandardCharsets.UTF_8));
             PrintWriter os = new PrintWriter(new OutputStreamWriter(
                     new FileOutputStream(dstPathName), StandardCharsets.UTF_8))) {

            // Read from src file and write to dest file
            String line;
            while ((line = is.readLine()) != null) {
                os.println(line);
            }
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Convert to html the file in srcPathName and place it in dstPathName
     *
     * @param srcPathName source path of the markdown file to convert
     * @param dstPathName destination path of the converted markdown file
     * @apiNote use the same file name for src and dst, this function changes the '.md' to '.html'
     */
    private void convertToHtml(String srcPathName, String dstPathName) {

        try (BufferedReader is = new BufferedReader(new InputStreamReader(
                new FileInputStream(srcPathName), StandardCharsets.UTF_8));
             PrintWriter os = new PrintWriter(new OutputStreamWriter(
                     new FileOutputStream(dstPathName.split(".md$")[0] + ".html"), StandardCharsets.UTF_8))) {

            // Read index file
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = is.readLine()) != null) {
                content.append(line).append("\n");
            }


            Markdown markdown = new Markdown();
            String data = markdown.split(content.toString())[1];

            // Convert to html
            String html = markdown.toHtml(data);

            // Create and write to index.html
            os.print(html);
            os.flush();

        } catch (FileNotFoundException e) {
            System.err.println(srcPathName + " was not found, please check that you \"init\" before \"build\"");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
