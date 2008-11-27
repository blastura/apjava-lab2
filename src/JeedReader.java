/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-11-27 23:53:24 anton"
 */

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.FilenameFilter;

/** Controller */
public class JeedReader {
    public static final File CONF_DIR = new File("config");
    private static Logger logger = Logger.getLogger("jeedreader");
    
    private JeedModel jeedModel;
    private JeedView jeedView;
    private JeedConfigWriter jeedWriter;
    private FeedUtil feedUtil;
    
    /**
     * Creates Model and View for this application, parses stored Feeds and
     * displays a GUI.
     */
    public JeedReader() {
        this.jeedModel = new JeedModel();
        this.jeedView = new JeedView(jeedModel);
        this.jeedWriter = new JeedConfigWriter();
        this.feedUtil = new FeedUtil();
        
        // TODO - Parse all feeds in CONF_DIR, add to JeedModel.
        System.out.println("FeedfileNames: ");
        File[] files = getFeedFiles();
        for (File file : files) {
            // TODO makeFeeds jss
            System.out.println(file.getName());
        }
        
        Feed feed = feedUtil.makeFeed("http://sydsvenskan.se/senastenytt/?context=senasteNyttRss");
        jeedModel.addFeed(feed);
        jeedWriter.saveFeed(feed);

        Feed feed2 = feedUtil.makeFeed("http://www.dn.se/DNet/custom-jsp/rss.jsp?d=1399&numItems=20");
        jeedModel.addFeed(feed2);
        jeedWriter.saveFeed(feed2);
        
        // Show GUI
        jeedView.setVisible(true);
    }

    /**
     * Returns an array containing all files in CONF_DIR which end with
     * feed.xml, these files should contain saved feeds.
     *
     * @return An array with feed files.
     */
    public File[] getFeedFiles() {
        if (JeedReader.CONF_DIR.isDirectory()) {
            File[] feedFiles = JeedReader.CONF_DIR.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (JeedReader.CONF_DIR.equals(dir)
                                && name.matches(".*feed.xml"));
                    }
                });
            return feedFiles;
        } else {
            return null;
        }
    }
    
    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
    
}
