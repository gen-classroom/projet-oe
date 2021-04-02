package ch.heigvd.gen.oe.command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.zip.InflaterOutputStream;

import ch.heigvd.gen.oe.utils.DFSFileExplorer;
import ch.heigvd.gen.oe.utils.Markdown;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "build", description = "Build a static site")
public class Build implements Callable<Integer> {

    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    @Override
    public Integer call() throws Exception {

        // Create build dir
        File build = new File(dirSiteName + "/build");
        if (build.mkdir()) {
            System.out.println("The directory " + build.getName() + " has been created!");
        } else {
            System.out.println("The directory " + build.getName() + " already exists");
        }

        // Create pages directories
        DFSFileExplorer dfsPre = new DFSFileExplorer((File file) -> {

            String path = file.getPath();
            String newPath = path.substring(0, dirSiteName.length())
                    + "/build" + path.substring(dirSiteName.length());

            if (file.isDirectory()) {
                File dir = new File(newPath);
                if (dir.mkdir()) {
                    System.out.println("The directory " + dir.getName() + " has been created!");
                } else {
                    System.out.println("The directory " + dir.getName() + " already exists");
                }
            }

            // Markdown file
            else if (file.getName().endsWith(".md")) {
                convertToHtml(path, newPath);
            }

            // Copy file
            else {
                copy(path, newPath);
            }

        }, true);

        dfsPre.visit(new File(dirSiteName + "/pages"));

        //TODO index a faire

        return 0;
    }

    private void copy(String srcFileName, String dstFileName) {

        try (BufferedReader is = new BufferedReader(new InputStreamReader(
                new FileInputStream(srcFileName), StandardCharsets.UTF_8));
             PrintWriter os = new PrintWriter(new OutputStreamWriter(
                     new FileOutputStream(dstFileName), StandardCharsets.UTF_8))) {

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


    private void convertToHtml(String srcFileName, String dstFileName) {

        try (BufferedReader is = new BufferedReader(new InputStreamReader(
                new FileInputStream(srcFileName), StandardCharsets.UTF_8));
             PrintWriter os = new PrintWriter(new OutputStreamWriter(
                     new FileOutputStream(dstFileName.split(".md$")[0] + ".html"), StandardCharsets.UTF_8))) {

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
            System.err.println(srcFileName + " was not found, please check that you \"init\" before \"build\"");

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
