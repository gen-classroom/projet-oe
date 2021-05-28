package ch.heigvd.gen.oe.structure;

/**
 * Class Page defining the elements of a web page
 * <p>
 * authors: Do Vale Lopes Miguel, Gamboni Fiona
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
     * @param content html content of the page
     * @param date    date of the page creation
     * @param title   title of the page
     * @param author  author of the page
     * @param config  config of the page
     */
    public Page(String content, String title, String author, String date, String filename, Config config) {
        this.content = content;
        this.date = date;
        this.title = title;
        this.author = author;
        this.filename = filename;
        this.site = config;
    }

    public String getMenu() {return menu;}

    public void setMenu(String menu) { this.menu = menu; }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getFilename() {
        return filename;
    }

    public Config getSite() {
        return site;
    }

}
