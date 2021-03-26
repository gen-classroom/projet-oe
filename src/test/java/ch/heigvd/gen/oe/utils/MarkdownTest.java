package ch.heigvd.gen.oe.utils;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        String inputMarkdown = """
                # Sample Markdown
                                
                This is some basic, sample markdown.
                                
                ## Second Heading
                                
                 * Unordered lists, and:
                                
                1.  One
                2.  Two
                3.  Three
                                
                 * More
                                
                > Blockquote
                                
                And **bold**, *italics*, and even *italics and later **bold***. Even ~~strikethrough~~. [A link](https://markdowntohtml.com) to somewhere.
                                
                And code highlighting:
                                
                ```js
                var foo = 'bar';
                                
                function baz(s) {
                   return foo + ':' + s;
                }
                ```
                                
                Or inline code like `var foo = 'bar';`.
                                
                Or an image of bears
                                
                ![bears](http://placebear.com/200/200)
                                
                The end ...
                """;

        String outputHtml;

        final String correctHtml = """
                <h1>Sample Markdown</h1>
                <p>This is some basic, sample markdown.</p>
                <h2>Second Heading</h2>
                <ul>
                <li>Unordered lists, and:</li>
                </ul>
                <ol>
                <li>One</li>
                <li>Two</li>
                <li>Three</li>
                </ol>
                <ul>
                <li>More</li>
                </ul>
                <blockquote>
                <p>Blockquote</p>
                </blockquote>
                <p>And <strong>bold</strong>, <em>italics</em>, and even <em>italics and later <strong>bold</strong></em>. Even <del>strikethrough</del>. <a href="https://markdowntohtml.com">A link</a> to somewhere.</p>
                <p>And code highlighting:</p>
                <pre><code class="language-js">var foo = 'bar';
                                
                function baz(s) {
                   return foo + ':' + s;
                }
                </code></pre>
                <p>Or inline code like <code>var foo = 'bar';</code>.</p>
                <p>Or an image of bears</p>
                <p><img src="http://placebear.com/200/200" alt="bears" /></p>
                <p>The end ...</p>
                """;

        outputHtml = markdown.toHtml(inputMarkdown);

        assertEquals(correctHtml,outputHtml);
    }

    @Test
    public void getMetadataShouldSeparateMetadataAndMarkdownAndPlaceThemInTwoStrings() {
        String markDownWithMetadata =
                """
                titre: Mon premier article
                auteur: Bertil Chapuis
                date: 2021-03-10
                ==!==
                # Mon premier article
                ## Mon sous-titre
                Le contenu de mon article.
                ![Une image](./image.png)""";

        String[] expectedOutput = {
                """
                titre: Mon premier article
                auteur: Bertil Chapuis
                date: 2021-03-10""",
                """ 
                # Mon premier article
                ## Mon sous-titre
                Le contenu de mon article.
                ![Une image](./image.png)"""
        };

        String[] actualOutput = markdown.getMetadata(markDownWithMetadata);

        assertArrayEquals(expectedOutput, actualOutput);
    }
}
