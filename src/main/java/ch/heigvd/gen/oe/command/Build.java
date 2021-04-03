package ch.heigvd.gen.oe.command;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import ch.heigvd.gen.oe.utils.Markdown;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Subcommand init
 *
 * author: Miguel Do Vale Lopes
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
            System.out.println("The directory " + build.getName() + " already exists");
        }

        // Treat index.md
        convertToHtml(dirSiteName + "/index.md", dirSiteName + "/build/index.md");

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
                    System.out.println("The directory " + dir.getName() + " already exists");
                }
            }

            // File is a markdown file -> convert to html
            else if (file.getName().endsWith(".md")) {
                convertToHtml(path, newPath);
            }

            // Copy file
            else {
                copy(path, newPath);
            }

        }, true);

        try {
            dfsPre.visit(new File(dirSiteName + "/pages"));

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return 0;
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

            // Get rid of metadata
            Markdown markdown = new Markdown();
            String data = markdown.getMetadata(content.toString())[1];

            // Convert to html
            String html = markdown.toHtml(data.toString());

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
