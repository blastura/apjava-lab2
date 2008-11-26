/*
 * @(#)JeedConfigParser.java
 * Time-stamp: "2008-11-26 21:49:41 anton"
 */

import java.io.File;
import java.io.FilenameFilter;
import org.jdom.Element;

public class JeedConfigParser extends RssParser { 
    // private Document doc;
    private static final File CONF_DIR = new File("config");
    private FeedOutputter feedOutputter;
    
    public JeedConfigParser() {
        this.feedOutputter = new FeedOutputter();
    }

    @Override
    public void parseItem(Element itemElement, RssItem rssItem) {
        boolean isRead = new Boolean(itemElement.getAttributeValue("isRead"));
        ((JeedItem) rssItem).setIsRead(isRead);
        super.parseItem(itemElement, rssItem);
    }

    
    /**
     * Returns an array containing all files in CONF_DIR which end with
     * feed.xml, these files should contain saved feeds.
     *
     * @return An array with feed files.
     */
    public File[] getFeedFiles() {
        System.out.println(CONF_DIR.getAbsolutePath());
        if (CONF_DIR.isDirectory()) {
            File[] feedFiles = CONF_DIR.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (CONF_DIR.equals(dir)
                                && name.matches(".*feed.xml"));
                    }
                });
            return feedFiles;
        } else {
            return null;
        }
    }
}