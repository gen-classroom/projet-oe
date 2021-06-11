package ch.heigvd.gen.oe.command;

import java.io.File;
import java.util.concurrent.Callable;

import ch.heigvd.gen.oe.structure.Config;
import ch.heigvd.gen.oe.utils.HandlebarsManager;
import ch.heigvd.gen.oe.utils.Json;
import ch.heigvd.gen.oe.utils.Markdown;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Subcommand init
 *
 * @author Fiona Gamboni, Miguel Do Vale Lopes
 */
@Command(name = "init", description = "Init a static site")
public class Init implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "</path/site>", description = "directory to contain the site")
    private String dirSiteName;

    /**
     * Call subcommand init to create the base of the site
     *
     * @return 0
     */
    @Override
    public Integer call() {
        // create site and pages directories
        createDir(dirSiteName);
        createDir(dirSiteName + "/pages");

        // create index.md
        Markdown index = new Markdown();
        index.create(dirSiteName + "/index");

        // create config.json
        Json config = new Json();
        config.create(new Config(), dirSiteName + "/config");

        // create templates
        HandlebarsManager hbManager = new HandlebarsManager(dirSiteName);
        hbManager.createTemplate();

        return 0;
    }

    /**
     * Create a directory
     *
     * @param dirName : name of the directory to be created
     */
    private void createDir(String dirName) {
        File dir = new File(dirName);
        boolean create = dir.mkdir();
        if (create) {
            System.out.println("The directory " + dirName + " has been created!");
        } else {
            System.out.println("The directory " + dirName + " already exists, files will be overwritten");
        }
    }
}
