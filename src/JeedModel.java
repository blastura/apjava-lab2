/*
 * @(#)JeedModel.java
 * Time-stamp: "2008-11-30 02:45:52 anton"
 */

import java.util.Vector;
import java.util.logging.Logger;
import java.util.Observable;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class JeedModel extends Observable {
    private static Logger logger = Logger.getLogger("jeedreader");
    private Vector<Feed> feeds;

    public JeedModel() {
        this.feeds = new Vector<Feed>();
    }

    public void addFeed(Feed feed) {
        feeds.add(feed);
        setChanged();
        notifyObservers();
    }

    public void updateFeeds() {
        // TODO
        boolean stateChanged = false;
        for (Feed feed : feeds) {
            URL feedLink = feed.getFeedLink();
            Feed updateFeed = FeedUtil.makeFeed(feedLink);            
            List<FeedItem> items = feed.getItems();
            List<FeedItem> updateItems = updateFeed.getItems();
            // List to store new items that only exist in updateItems.
            List<FeedItem> newItems = new ArrayList<FeedItem>();
            
            // TODO, should check dates, and HttpHeader to reduce searchtime?
            // O(n^2)
            for (FeedItem updateItem: updateItems) {
                boolean isOldItem = false;
                for (FeedItem item : items) {
                    if (item.equals(updateItem)) {
                        // New item found
                        isOldItem = true;
                    }
                }
                if (!isOldItem || items.isEmpty()) {
                    logger.info("New item found: " + updateItem
                                + " in feed: " + feed);
                    stateChanged = true;
                    newItems.add(updateItem);
                }
            }

            // Add all new Items to current feed.
            for (FeedItem newItem : newItems) {
                feed.addItem(newItem);
            }
        }
        
        // If new Items are found notify all Observers of this class.
        if (stateChanged) {
            setChanged();
            notifyObservers();
        }
    }
    
    // TODO is this the really neccesary?
    public FeedItem[] getItemsForFeed(Feed feed) {
        return feed.getItems().toArray(new FeedItem[0]);
    }

    // TODO is this really the way to go???? toString()?
    public String getDescribtionFromFeedItem(FeedItem feedItem) {
        return "<h1>" + feedItem + "</h1>"
            + "<p>" + feedItem.getDescribtion() + "</p>";
    }

    public Vector<Feed> getFeeds() {
        return feeds;
    }

    public void removeFeed(Feed feed) {
        feeds.remove(feed);
    }
}
