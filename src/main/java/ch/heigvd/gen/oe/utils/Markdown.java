package ch.heigvd.gen.oe.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(markdown);
        return renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
    }

    /**
     * Separate the metadata and markdown data
     * @param markdown with metadata separated with ==!==
     * @return String array of size 2, first is data, second is markdown data
     * @throws RuntimeException - is the markdown doesn't have the ==!== separator
     */
    public String[] getMetadata(String markdown) throws RuntimeException {
        if (markdown.contains("==!==")) {
            return markdown.split("==!==", 2);
        } else {
            throw new RuntimeException("Missing separator in markdown file : Missing ==!==");
        }
    }

}
