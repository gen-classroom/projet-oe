package ch.heigvd.gen.oe.utils;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Generate Markdown page
 *
 * author: Fiona Gamboni, Gaétan Zwick
 */
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
    public File create(String filename) {
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
    public File create() {
        return create("index");
    }

    /**
     * Create the page based on the template
     * @param page : the page to be created
     * @return the new page
     * @throws IOException
     */
    private File pageCreation(File page) throws IOException {
        if (page.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists, file will be overwritten");
        }
        FileWriter writer = new FileWriter(page);
        writer.write(TEMPLATE);
        writer.close();
        return page;
    }

    /**
     * Convert markdown data to html format string
     * @param markdown without metadata
     * @return String of markdown in html format
     */
    public String toHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    /**
     * Separate the metadata and markdown data
     * @param markdown with metadata separated with ==!==
     * @return String array of size 2, first is data, second is markdown data
     * @throws RuntimeException - is the markdown doesn't have the ==!== separator
     */
    // todo : support other line breaks ?
    public String[] getMetadata(String markdown) throws RuntimeException {
        if (markdown.contains("\n==!==\n")) {
            return markdown.split("\n==!==\n", 2);
        } else {
            throw new RuntimeException("Missing separator in markdown file : Missing ==!==");
        }
    }

}
