/*
 * @(#)JeedModel.java
 * Time-stamp: "2008-12-01 01:10:07 anton"
 */

import java.util.Vector;
import java.util.logging.Logger;
import java.util.Observable;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import org.jdom.JDOMException;

public class JeedModel extends Observable {
    private static Logger logger = Logger.getLogger("jeedreader");
    private Vector<Feed> feeds;

    public JeedModel() {
        this.feeds = new Vector<Feed>();
    }

    public void addFeed(Feed feed) {
        feeds.add(feed);
        setChanged();
        notifyObservers(feed);
    }

    public boolean updateFeed(Feed feed) throws IOException, JDOMException {
        boolean isChanged = false;

        List<FeedItem> newItems = new ArrayList<FeedItem>();
        URL feedLink = feed.getFeedLink();
        Feed updateFeed = FeedUtil.makeFeed(feedLink);
        List<FeedItem> items = feed.getItems();
        List<FeedItem> updateItems = updateFeed.getItems();
        // List to store new items that only exist in updateItems.

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
                isChanged = true;
                newItems.add(updateItem);
            }
        }

        // Add all new Items to current feed.
        for (FeedItem newItem : newItems) {
            feed.addItem(newItem);
        }
        return isChanged;
    }

    public void updateFeeds() throws IOException, JDOMException {
        boolean stateChanged = false;
        for (Feed feed : feeds) {
            stateChanged = updateFeed(feed);
        }

        // If new Items are found notify all Observers of this class.
        if (stateChanged) {
            setChanged();
            // TODO constant newItems
            notifyObservers("newItems");
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
