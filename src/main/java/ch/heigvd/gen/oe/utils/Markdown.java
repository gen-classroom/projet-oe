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

    static final char LINEBREAK_TYPE = '\n';
    static final String METADATA_SEPARATOR = "---";

    private static final String TEMPLATE = "titre:" + LINEBREAK_TYPE +
            "auteur:" + LINEBREAK_TYPE +
            "date: " + java.time.LocalDate.now() + LINEBREAK_TYPE +
            METADATA_SEPARATOR + LINEBREAK_TYPE + "#Write your page in markdown";

    /**
     * Create a new markdown page with a given name
     * @param filename : filename
     */
    public void create(String filename) {
        try {
            File page = new File(filename + ".md");
            if (page.createNewFile()) {
                System.out.println("File " + filename + ".md has been created!");
            } else {
                System.out.println("File " + filename + ".md already exists, file will be overwritten");
            }
            FileWriter writer = new FileWriter(page);
            writer.write(TEMPLATE);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @param markdown with metadata separated by LINEBREAK_TYPE + METADATA_SEPARATOR + LINEBREAK_TYPE
     * @return String array of size 2, first is data, second is markdown data
     * @throws RuntimeException - is the markdown doesn't have the separator
     */
    public String[] getMetadata(String markdown) throws RuntimeException {
        String separator = LINEBREAK_TYPE + METADATA_SEPARATOR + LINEBREAK_TYPE;
        if (markdown.contains(separator)) {
            return markdown.split(separator, 2);
        } else {
            throw new RuntimeException("Missing separator in markdown file");
        }
    }

}
