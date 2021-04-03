package ch.heigvd.gen.oe.structure;

/**
 * Class Config defining the general metadatas of the static site
 *
 * @author Do Vale Lopes Miguel
 */
public class Config {
    private String title = "";
    private String description = "";
    private String domain = "";
    private String language = "";

    public String getDescription() {
        return description;
    }

    public String getDomain() {
        return domain;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }
}
