package ch.heigvd.gen.oe.structure;

/**
 * Class Config defining the general metadatas of the static site
 *
 * @author Do Vale Lopes Miguel
 */
public class Config {
    private final String title;
    private final String description;
    private final String domain;
    private final String language;

    /**
     * Default constructor of Config
     */
    public Config() {
        this.title = "";
        this.description = "";
        this.domain = "";
        this.language = "";
    }

    /**
     * Constructor of Config
     *
     * @param title       title of the website
     * @param description description of the website
     * @param domain      domain of the website
     * @param language    language of the website
     */
    public Config(String title, String description, String domain, String language) {
        this.title = title;
        this.description = description;
        this.domain = domain;
        this.language = language;
    }

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
