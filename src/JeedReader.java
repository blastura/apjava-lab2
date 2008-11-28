/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-11-29 00:01:22 anton"
 */

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.FilenameFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

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
        File[] feedFiles = getFeedFiles();
        for (File file : feedFiles) {
            logger.info(file.getName());
            Feed feed = feedUtil.makeFeed(file);
            jeedModel.addFeed(feed);
            jeedWriter.saveFeed(feed);
        }
        
        //         Feed feed = feedUtil.makeFeed("http://sydsvenskan.se/senastenytt/?context=senasteNyttRss");
        //         jeedModel.addFeed(feed);
        //         jeedWriter.saveFeed(feed);

        //         Feed feed2
        //             = feedUtil.makeFeed("http://www.dn.se/DNet/custom-jsp/rss.jsp?d=1399&numItems=20");
        //         jeedModel.addFeed(feed2);
        //         jeedWriter.saveFeed(feed2);
        
        // Add Listeners
        jeedView.setAddFeedListener(new AddFeedListener());
        jeedView.setFeedListListener(new FeedListListener());
        jeedView.setItemListListener(new ItemListListener());

        // Show View
        jeedView.showView();
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

    private class AddFeedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            logger.info("Add a feed");
            String feedLink = jeedView.promtForFeedLink();
            if (!FeedUtil.isURL(feedLink)) {
                System.err.println("Url is not wellformed: " + feedLink);
                return;
            }
            logger.info("Input URL: " + feedLink);
            Feed feed
                = feedUtil.makeFeed(feedLink);
            jeedModel.addFeed(feed);
            jeedWriter.saveFeed(feed);
            jeedView.refreshFeeds();
        }
    }

    private class FeedListListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            logger.info("Feed selected");
            JeedReader.this.jeedView.showItemsForCurrentFeedSelection();
        }
    }

    private class ItemListListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            logger.info("Item selected");
            JeedReader.this.jeedView.showDescForCurrentItemSelection();
        }
    }

    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
}