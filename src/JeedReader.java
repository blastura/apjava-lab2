/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-11-30 18:20:33 anton"
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** Controller */
public class JeedReader {
    public static final File CONF_DIR = new File("config");
    private int feedUpdateInterval =  1 * 60 * 1000;
    private static Logger logger = Logger.getLogger("jeedreader");
    
    private JeedModel jeedModel;
    private JeedView jeedView;
    private JeedConfigWriter jeedWriter;
    
    /**
     * Creates Model and View for this application, parses stored Feeds and
     * displays a GUI.
     */
    public JeedReader() {
        this.jeedModel = new JeedModel();
        this.jeedView = new JeedView(jeedModel);
        this.jeedWriter = new JeedConfigWriter();
        
        // TODO - Parse all feeds in CONF_DIR, add to JeedModel.
        System.out.println("FeedfileNames: ");
        File[] feedFiles = getFeedFiles();
        for (File file : feedFiles) {
            logger.info(file.getName());
            Feed feed = FeedUtil.makeFeed(file);
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
        
        // Add Listeners and Observers
        jeedView.setUpdateFeedListener(new UpdateFeedListener());
        jeedView.setAddFeedListener(new AddFeedListener());
        jeedView.setFeedListListener(new FeedListListener());
        jeedView.setItemListListener(new ItemListListener());
        
        jeedModel.addObserver(jeedView);
        
        // Start updates by intervall
        this.startUpdateFeedByInterval();
        
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
            File[] feedFiles
                = JeedReader.CONF_DIR.listFiles(new FilenameFilter() {
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

    public void setUpdateFeedsInterval(int feedUpdateInterval) {
        this.feedUpdateInterval = feedUpdateInterval;
    }
    
    public void startUpdateFeedByInterval() {
        new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        logger.info("Thread is: " + Thread.currentThread().toString());
                        jeedModel.updateFeeds();
                        try {
                            Thread.sleep(feedUpdateInterval);
                        }
                        catch (InterruptedException e) {
                            logger.warning("updateFeedByInterval was interrupted");
                        }
                    }
                }
            }, "updateFeedByIntervalThread").start();
    }

    private class UpdateFeedListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            logger.info("Update feeds");
            // TODO Threads
            new Thread(new Runnable() {
                    public void run() {
                        jeedModel.updateFeeds();
                    }
                }).start();
        }
    }

    private class AddFeedListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            new Thread(new Runnable() {
                    public void run() {
                        logger.info("Thread is: " + Thread.currentThread().toString());
                        
                        String urlString = jeedView.promtForFeedUrlString();
                        if (!FeedUtil.isURL(urlString)) {
                            System.err.println("Url is not wellformed: " + urlString);
                            return;
                        }
                        
                        try {
                            // Could take some time, should be run on a different thread.
                            Feed feed = FeedUtil.makeFeed(urlString);                
                            jeedModel.addFeed(feed);
                            jeedWriter.saveFeed(feed);
                            // DONE observable jeedView.refreshFeeds();
                        } catch (MalformedURLException e) {
                            //TODO - fix error message
                            logger.info("Input URL string: " + urlString);
                            e.printStackTrace();
                        }
                    }
                }).start();
        }
    }
    
    private class FeedListListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            logger.info("Thread is: " + Thread.currentThread().toString());
            if (e.getValueIsAdjusting() == false) {
                logger.info("Feed changed");
                JeedReader.this.jeedView.showItemsForCurrentFeedSelection();
            }
        }
    }

    private class ItemListListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            logger.info("Thread is: " + Thread.currentThread().toString());
            if (e.getValueIsAdjusting() == false) {
                logger.info("Item changed");
                JeedReader.this.jeedView.showDescForCurrentItemSelection();
            }
        }
    }

    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
}