/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-12-01 23:41:07 anton"
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdom.JDOMException;

/** Controller */
public class JeedReader implements Observer {
    public static final File CONF_DIR = new File("config");
    private Thread updateThread;
    private int feedUpdateInterval =  5 * 60 * 1000;
    private static Logger logger = Logger.getLogger("jeedreader");
    
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
                    setUpdateIntervalPromt("");
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
        
        // Start updates by intervall
        this.startUpdateFeedByInterval();
        
        // Show View
        jeedView.initView();
    }
    
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
    
    
    public void setUpdateFeedsInterval(int feedUpdateInterval) {
        this.feedUpdateInterval = feedUpdateInterval;
    }
    
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
                        // TODO handle nullpointer display error message
                    } catch (IllegalArgumentException e) {
                        logger.warning("IllegalArgumentException");
                        addFeedPromt(e.getMessage() + "\n");
                    } catch (MalformedURLException e) {
                        // TODO - fix error message
                        logger.warning("Input URL string: " + urlString);
                        addFeedPromt("Malformed URL.\n");
                        //e.printStackTrace();
                    } catch (IOException e) {
                        // TODO - Handle error with View.
                        logger.warning("IOException");
                        addFeedPromt("Resource not found.\n");
                        // e.printStackTrace();
                    } catch (JDOMException e) {
                        // TODO - fix error message
                        addFeedPromt("Couldn't parse resource.\n");
                        logger.warning("JDOMException");
                        // e.printStackTrace();
                    }
                }
            }).start();
    }
    
    private class UpdateFeedsListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            logger.info("Update feeds");
            // TODO Threads
            new Thread(new Runnable() {
                    public void run() {
                        tryUpdateFeeds();
                    }
                }).start();
        }
    }

    private class UpdateFeedListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            logger.info("Update feed");
            // TODO Threads
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
    
    private class AddFeedListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            addFeedPromt("");
        }
    }
    
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
                            JeedReader.this.jeedView.showItemsForCurrentFeedSelection();
                        }
                    }).start();
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

    private class ClosingListener extends WindowAdapter implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            logger.info("Closing program");
            JeedConfigWriter.saveFeeds(jeedModel.getFeeds());
            System.exit(0);
        }
        
        @Override
        public void windowClosing(WindowEvent we) {
            logger.info("Window closeing");
            JeedConfigWriter.saveFeeds(jeedModel.getFeeds());
            System.exit(0);
        }
    }

    public void update(Observable obj, Object arg) {
        // TODO Am I saving some other place aswell?????        
        if (arg instanceof Feed) {
            logger.info("New feed detected");
            Feed feed = (Feed) arg;
            JeedConfigWriter.saveFeed(feed);
        }
    }

    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
}