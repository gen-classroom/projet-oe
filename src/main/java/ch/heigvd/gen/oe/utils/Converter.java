package ch.heigvd.gen.oe.utils;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class Converter {

    /**
     * Convert markdown data to html format string
     * @param markdown without metadata
     * @return String of markdown in html format
     */
    public static String markdownToHtml(String markdown) {
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
     */
    public static String[] getMetadataFromMarkdown(String markdown) throws RuntimeException {
        if (markdown.contains("==!==")) {
            return markdown.split("==!==", 2);
        } else {
            throw new RuntimeException("Missing separator in markdown file : Missing ==!==");
        }
    }
}
