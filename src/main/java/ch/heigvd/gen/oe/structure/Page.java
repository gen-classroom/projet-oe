package ch.heigvd.gen.oe.structure;

/**
 * Class Page defining the elements of a web page
 *
 * @author Do Vale Lopes Miguel, Gamboni Fiona
 */
public class Page {
    private final String content;
    private final String date;
    private final String title;
    private final String author;
    private final String filename;
    private final Config site;
    private String menu = "";

    /**
     * Page constructor
     *
     * @param content   html content of the page
     * @param date      date of the page creation
     * @param title     title of the page
     * @param author    author of the page
     * @param filename  filename of the page
     * @param config    config of the page
     */
    public Page(String content, String title, String author, String date, String filename, Config config) {
        this.content = content;
        this.date = date;
        this.title = title;
        this.author = author;
        this.filename = filename;
        this.site = config;
    }

    /**
     * @return menu of the page
     */
    public String getMenu() {return menu;}

    /**
     * @param menu new menu to set
     */
    public void setMenu(String menu) { this.menu = menu; }

    /**
     * @return content of the page
     */
    public String getContent() {
        return content;
    }

    /**
     * @return date of the page
     */
    public String getDate() {
        return date;
    }

    /**
     * @return title of the page
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return author of the page
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return filename of the page
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return config
     */
    public Config getSite() {
        return site;
    }

}
