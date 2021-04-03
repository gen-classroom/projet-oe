package ch.heigvd.gen.oe.utils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests of methodes create(), pageCreation(),
 * toHtml(), getMetadata() from class Markdown
 *
 * @author Gaétan Zwick
 */
public class MarkdownTest {
    Markdown markdown = new Markdown();

    @Test
    public void toHtmlShouldReturnTheMarkdownStringinHtmlFormat() {

        String inputMarkdown = "# Sample Markdown\n"
                + "This is some basic, sample markdown.\n"
                + "## Second Heading\n"
                + "* Unordered lists, and:\n"
                + "1.  One\n"
                + "2.  Two\n"
                + "3.  Three\n"
                + "* More\n"
                + "> Blockquote\n"
                + "\n"
                + "And **bold**, *italics*, and even *italics and later **bold***. Even ~~strikethrough~~. [A link](https://markdowntohtml.com) to somewhere.\n"
                + "\n"
                + "And code highlighting:\n"
                + "```js\n"
                + "var foo = 'bar';\n"
                + "function baz(s) {\n"
                + "return foo + ':' + s;\n"
                + "}\n"
                + "```\n"
                + "Or inline code like `var foo = 'bar';`.\n"
                + "\n"
                + "Or an image of bears\n"
                + "\n"
                + "![bears](http://placebear.com/200/200)\n"
                + "\n"
                + "The end ...";

        String outputHtml;

        final String correctHtml = "<h1>Sample Markdown</h1>\n"
                + "<p>This is some basic, sample markdown.</p>\n"
                + "<h2>Second Heading</h2>\n"
                + "<ul>\n"
                + "<li>Unordered lists, and:</li>\n"
                + "</ul>\n"
                + "<ol>\n"
                + "<li>One</li>\n"
                + "<li>Two</li>\n"
                + "<li>Three</li>\n"
                + "</ol>\n"
                + "<ul>\n"
                + "<li>More</li>\n"
                + "</ul>\n"
                + "<blockquote>\n"
                + "<p>Blockquote</p>\n"
                + "</blockquote>\n"
                + "<p>And <strong>bold</strong>, <em>italics</em>, and even <em>italics and later <strong>bold</strong></em>. Even <del>strikethrough</del>. <a href=\"https://markdowntohtml.com\">A link</a> to somewhere.</p>\n"
                + "<p>And code highlighting:</p>\n"
                + "<pre><code class=\"language-js\">var foo = 'bar';\n"

                + "function baz(s) {\n"
                + "return foo + ':' + s;\n"
                + "}\n"
                + "</code></pre>\n"
                + "<p>Or inline code like <code>var foo = 'bar';</code>.</p>\n"
                + "<p>Or an image of bears</p>\n"
                + "<p><img src=\"http://placebear.com/200/200\" alt=\"bears\" /></p>\n"
                + "<p>The end ...</p>\n";


        outputHtml = markdown.toHtml(inputMarkdown);

        assertEquals(correctHtml, outputHtml);
    }


    @Test
    public void getMetadataShouldSeparateMetadataAndMarkdownAndPlaceThemInTwoStrings() {
        String markDownWithMetadata = "titre: Mon premier article\n"
                + "auteur: Bertil Chapuis\n"
                + "date: 2021-03-10\n"
                + Markdown.LINEBREAK_TYPE + Markdown.METADATA_SEPARATOR + Markdown.LINEBREAK_TYPE
                + "# Mon premier article\n"
                + "## Mon sous-titre\n"
                + "Le contenu de mon article.\n"
                + "![Une image](./image.png)";

        String[] expectedOutput = {
                "titre: Mon premier article\n"
                        + "auteur: Bertil Chapuis\n"
                        + "date: 2021-03-10\n",
                "# Mon premier article\n"
                        + "## Mon sous-titre\n"
                        + "Le contenu de mon article.\n"
                        + "![Une image](./image.png)"
        };

        String[] actualOutput = markdown.getMetadata(markDownWithMetadata);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void getMetadataShouldReturnRuntimeErrorWhenNoOrWrongSeparatorInGivenMarkdown() {

        String markDownWithMetadataButNoSeparator = "titre: Mon premier article\n"
                + "auteur: Bertil Chapuis\n"
                + "date: 2021-03-10\n"
                + "# Mon premier article\n"
                + "## Mon sous-titre\n"
                + "Le contenu de mon article.\n"
                + "![Une image](./image.png)";

        String markDownWithMetadataButWrongSeparator = "titre: Mon premier article\n"
                + "auteur: Bertil Chapuis\n"
                + "date: 2021-03-10\n"
                + "--oe-\n"
                + "# Mon premier article\n"
                + "## Mon sous-titre\n"
                + "Le contenu de mon article.\n"
                + "![Une image](./image.png)";

        String markDownWithMetadataButMisplacedSeparator = "titre: Mon premier article\n"
                + "auteur: Bertil Chapuis\n"
                + "date: 2021-03-10-oe--\n"
                + "# Mon premier article\n"
                + "## Mon sous-titre\n"
                + "Le contenu de mon article.\n"
                + "![Une image](./image.png)";

        assertThrows(RuntimeException.class, () -> markdown.getMetadata(markDownWithMetadataButNoSeparator));
        assertThrows(RuntimeException.class, () -> markdown.getMetadata(markDownWithMetadataButWrongSeparator));
        assertThrows(RuntimeException.class, () -> markdown.getMetadata(markDownWithMetadataButMisplacedSeparator));
    }

    @Test
    public void createShouldCreateANewIndexPageFromTemplate() {

        final char LINEBREAK_TYPE = '\n';
        final String METADATA_SEPARATOR = "---";
        final String TEMPLATE = "titre:" + LINEBREAK_TYPE +
                "auteur:" + LINEBREAK_TYPE +
                "date: " + java.time.LocalDate.now() /* do not test at 23:59:59... */ + LINEBREAK_TYPE +
                METADATA_SEPARATOR + LINEBREAK_TYPE + "#Write your page in markdown";
        PrintWriter pw = null;

        try {
            File actualOutput = markdown.create("src/test/java/ch/heigvd/gen/oe/utils/tempFile/index_file_actual");
            File expectedOutput = new File("src/test/java/ch/heigvd/gen/oe/utils/tempFile/index_file_expected.md");
            pw = new PrintWriter(expectedOutput);
            pw.print(TEMPLATE);
            pw.flush();
            assertTrue(FileUtils.contentEquals(actualOutput, expectedOutput), "The files differ!");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert pw != null;
            pw.close();
        }

    }

}
