package ch.heigvd.gen.oe.command;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ch.heigvd.gen.oe.structure.Config;
import ch.heigvd.gen.oe.structure.Page;
import ch.heigvd.gen.oe.utils.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Subcommand build
 * <p>
 * author: Miguel Do Vale Lopes, Gamboni Fiona
 */
@Command(name = "build", description = "Build a static site")
public class Build implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to build site")
    private String dirSiteName;

    @CommandLine.Option(names = {"-w", "--watch"}, description = "build site every update")
    private static boolean watch;

    /**
     * Call subcommand build to build the initialised site
     *
     * @return 0
     */
    @Override
    public Integer call() {

        if (watch) {
            FileWatcher fileWatcher = null;
            try {
                fileWatcher = new FileWatcher();
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }

            try {
                final List<Path> paths = new ArrayList<>();
                WatchKey key = null;
                do {
                    if (key != null) {
                        key.reset();
                    }
                    build();
                    paths.clear();
                    paths.add(Paths.get(dirSiteName));
                    DFSFileExplorer dfs = new DFSFileExplorer((File file) -> {
                        if (file.isDirectory()) {
                            paths.add(Paths.get(file.getPath()));
                        }
                    }, false);

                    dfs.visit(new File(dirSiteName + "/pages"));
                    fileWatcher.watch(paths).reset();
                } while (true);

            } catch (InvocationTargetException | IllegalAccessException | IOException | InterruptedException e) {
                e.printStackTrace();
                return 1;
            }
        } else {
            build();
        }

        return 0;
    }


    private int build() {
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


    /**
     * Create an html File with html content
     *
     * @param html        String with html content
     * @param dstPathName path of the file to be created
     */
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

    /**
     * Read and get informations of a file to create a Page
     *
     * @param srcPathName path of the file
     * @param config      config
     * @return a new Page
     */
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
            String[] metadata = splitMetadata(mdData[0]);

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

    /**
     * Split the metadata to convert them to an array of String
     *
     * @param metadata String metadata
     * @return an array of String containing the metadata
     */
    private String[] splitMetadata(String metadata) {
        Object[] tmp = metadata.lines().toArray();
        String[] result = new String[tmp.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = ((String) tmp[i]).split(":", 2)[1];
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

}
