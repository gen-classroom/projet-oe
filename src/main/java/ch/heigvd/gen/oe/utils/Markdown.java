package ch.heigvd.gen.oe.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Markdown {

    private static final String TEMPLATE = "titre:\n" +
            "auteur:\n" +
            "date: "+java.time.LocalDate.now()+"\n" +
            "---\n#Write your page in markdown";

    /**
     * Create a new markdown page with a given name
     * @param filename : filename
     * @return the new page
     */
    public static File create(String filename) {
        try {
            File page = new File(filename + ".md");
            return pageCreation(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create the default markdown page named index.md
     * @return the new page
     */
    public static File create() {
        try {
            File page = new File("index.md");
            return pageCreation(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create the page based on the template
     * @param page : the page to be created
     * @return the new page
     * @throws IOException
     */
    private static File pageCreation(File page) throws IOException {
        if (page.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }
        FileWriter writer = new FileWriter(page);
        writer.write(TEMPLATE);
        writer.close();
        return page;
    }

}
