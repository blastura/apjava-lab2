/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-12-02 22:38:06 anton"
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdom.JDOMException;

/**
 * JeedReader is the main class of this application. It instantiates the other
 * classes and controls user action. This is the "Controller" of this program.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public class JeedReader implements Observer {
    private static Logger logger = Logger.getLogger("jeedreader");
    
    public static final File CONF_DIR = new File("config");
    private Thread updateThread;
    private int feedUpdateInterval =  5 * 60 * 1000;
    
    private JeedModel jeedModel;
    private JeedView jeedView;
    
    /**
     * Creates Model and View for this application, parses stored Feeds and
     * displays a GUI.
     */
    public JeedReader() {
        this.jeedModel = new JeedModel();
        this.jeedView = new JeedView(jeedModel);
        
        // Parse and add all feeds, in CONF_DIR, to jeedModel
        File[] feedFiles = FeedUtil.getFeedFiles();
        for (File file : feedFiles) {
            logger.info(file.getName());
            Feed feed = FeedUtil.makeFeed(file);
            jeedModel.addFeed(feed);
            JeedConfigWriter.saveFeed(feed);
        }
        
        // Add Listeners
        jeedView.addWindowListener(new ClosingListener());
        jeedView.setProgramCloseListeners(new ClosingListener());        

        jeedView.setUpdateIntervalListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    setUpdateIntervalPromt();
                }
            });
        jeedView.setUpdateFeedsListener(new UpdateFeedsListener());
        jeedView.setUpdateFeedListener(new UpdateFeedListener());
        jeedView.setAddFeedListener(new AddFeedListener());
        jeedView.setFeedListListener(new FeedListListener());
        jeedView.setItemListListener(new ItemListListener());

        // Add Observers
        jeedModel.addObserver(jeedView);
        jeedModel.addObserver(this);
        
        // Start updates by interval
        this.startUpdateFeedByInterval();
        
        // Show View
        jeedView.initView();
    }
    
    /**
     * Tries to update all feeds in jeedModel, sends possible exception as an
     * error message to the View.
     *
     */
    private void tryUpdateFeeds() {
        try {
            jeedModel.updateFeeds();
        } catch (IOException e) {
            //TODO - fix error message
            jeedView.showErrorMessage(e.getMessage());
        } catch (JDOMException e) {
            // TODO - fix error message
            jeedView.showErrorMessage(e.getMessage());
        }
    }
    
    /**
     * Tries to update one Feed, sends possible exception as an error message to
     * the View.
     *
     * @param feed The Feed to update.
     */
    private void tryUpdateFeed(Feed feed) {
        try {
            jeedModel.updateFeed(feed);
        } catch (IOException e) {
            //TODO - fix error message
            jeedView.showErrorMessage(e.getMessage());
        } catch (JDOMException e) {
            // TODO - fix error message
            jeedView.showErrorMessage(e.getMessage());
        }
    }
    
    /**
     * Starts a new Thread that updates all Feeds in <code>jeedModel</code> at
     * the specified interval <code>feedUpdateInterval</code>.
     *
     */
    public void startUpdateFeedByInterval() {
        this.updateThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        logger.info("Thread is: " + Thread.currentThread().toString());
                        // Update all feeds
                        tryUpdateFeeds();
                        // Save all feeds
                        JeedConfigWriter.saveFeeds(jeedModel.getFeeds());
                        // Wait for feedUpdateInterval milliseconds
                        try {
                            Thread.sleep(feedUpdateInterval);
                        }
                        catch (InterruptedException e) {
                            logger.info("updateFeedByInterval was interrupted");
                        }
                    }
                }
            }, "updateFeedByIntervalThread");
            this.updateThread.start();
    }
    
    /**
     * Calls setUpdateIntervalPromt(java.lang.String) with an empty string a a
     * parameter. This is used as the initial call to this method.
     */
    private void setUpdateIntervalPromt() {
        setUpdateIntervalPromt("");
    }
    
    /**
     * Shows a promt for a new <code>feedUpdateInterval</code> in the View, the
     * update Thread is then interrupted and replaced with a new Thread updates
     * the feeds at the new interval.
     *
     * @param errorMsg An error message to display if the user supplied string
     * can't be converted to an Integer. 
     */
    private void setUpdateIntervalPromt(final String errorMsg) {
        String result = jeedView.promtForUpdateInterval(errorMsg);
        if (result == null) {
            return;
        }
        
        try {
            int newInterval = Integer.parseInt(result);
            this.feedUpdateInterval = newInterval;
            logger.info("New update interval: " + this.feedUpdateInterval);
            this.updateThread.interrupt();
            startUpdateFeedByInterval();
        } catch (NumberFormatException e) {
            logger.warning("String does not contain a parsable integer.");
            setUpdateIntervalPromt("Supplied string must be a number.\n");
        }
    }
    
    /**
     * If a user wants to add a new feed, this method promts for a String that
     * should represent a feed URL. And then creates and adds the feed.
     *
     * @param errorMsg An error message if the user supplied String is not
     * valid.
     */
    private void addFeedPromt(final String errorMsg) {
        new Thread(new Runnable() {
                public void run() {
                    logger.info("Thread is: " + Thread.currentThread().toString());
                        
                    String urlString = jeedView.promtForFeedUrlString(errorMsg);
                    if (urlString == null) {
                        return;
                    }
                    
                    try {
                        Feed feed = FeedUtil.makeFeed(urlString);                
                        if (feed != null) {
                            jeedModel.addFeed(feed);
                            JeedConfigWriter.saveFeed(feed);
                        }
                    } catch (IllegalArgumentException e) {
                        logger.warning("IllegalArgumentException");
                        addFeedPromt(e.getMessage() + "\n");
                    } catch (MalformedURLException e) {
                        logger.warning("Input URL string: " + urlString);
                        addFeedPromt("Malformed URL.\n");
                    } catch (IOException e) {
                        // TODO - create more correct error messages
                        addFeedPromt("Resource not found or invalid.\n");
                        logger.warning("IOException " + e.getMessage());
                    } catch (JDOMException e) {
                        // TODO - create more correct error messages
                        addFeedPromt("Couldn't parse resource.\n");
                        logger.warning("JDOMException" + e.getMessage());
                    }
                }
            }).start();
    }
    
    /**
     * An inner class that listens to actions triggered from the GUI when a user
     * wants to update all feeds.
     */
    private class UpdateFeedsListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            logger.info("Update feeds");
            new Thread(new Runnable() {
                    public void run() {
                        tryUpdateFeeds();
                    }
                }).start();
        }
    }

    /**
     * An inner class that listens to actions triggered from the GUI when a user
     * wants to update the selected feed.
     *
     */
    private class UpdateFeedListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            logger.info("Update feed");
            new Thread(new Runnable() {
                    public void run() {
                        Feed feed = jeedView.getSelectedFeed();
                        if (feed != null) {
                            tryUpdateFeed(feed);
                        }
                    }
                }).start();
        }
    }
    
    /**
     * An inner class that handles events when a user wants to add a feed.
     */
    private class AddFeedListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            addFeedPromt("");
        }
    }
    
    /**
     * An inner class that handles selection changes in jeedViews feed list.
     */
    private class FeedListListener implements ListSelectionListener {
        public void valueChanged(final ListSelectionEvent e) {
            logger.info("Thread is: " + Thread.currentThread().toString());
            if (e.getValueIsAdjusting() == false) {
                logger.info("Feed changed");
                new Thread(new Runnable() {
                        public void run() {
                            Feed feed = jeedView.getSelectedFeed();
                            if (feed != null) {
                                tryUpdateFeed(feed);
                            }
                            // Updates UI from different thread.
                            SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        JeedReader.this.jeedView.showItemsForCurrentFeedSelection();
                                    }
                                });
                        }
                    }).start();
            }
        }
    }

    /**
     * An inner class that handles selection changes in jeedViews item list.
     *
     */
    private class ItemListListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            logger.info("Thread is: " + Thread.currentThread().toString());
            if (e.getValueIsAdjusting() == false) {
                logger.info("Item changed");
                JeedReader.this.jeedView.showDescForCurrentItemSelection();
            }
        }
    }

    /**
     * Describe class <code>ClosingListener</code> here.
     *
     */
    private class ClosingListener extends WindowAdapter implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            logger.info("Closing program");
            JeedConfigWriter.saveFeeds(jeedModel.getFeeds());
            System.exit(0);
        }
        
        @Override
        public void windowClosing(WindowEvent we) {
            logger.info("Window closing");
            JeedConfigWriter.saveFeeds(jeedModel.getFeeds());
            System.exit(0);
        }
    }

    /**
     * If a feed is changed in jeedModel this method is invoked and the feed is
     * saved to disk.
     *
     * @param obj The invoking Observable, in this case jeedModel.
     * @param arg should contain the feed to save.
     */
    public void update(Observable obj, Object arg) {
        // TODO Am I saving some other place as well?????        
        if (arg instanceof Feed) {
            logger.info("New feed detected, saving...");
            Feed feed = (Feed) arg;
            JeedConfigWriter.saveFeed(feed);
        }
    }

    /**
     * Starts the program.
     *
     * @param args no arguments should be supplied.
     */
    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
}