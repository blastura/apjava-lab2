/*
 * @(#)JeedModel.java
 * Time-stamp: "2008-12-03 12:26:49 anton"
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Vector;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * JeedModel stores all the feeds. Methods to add and update the feeds are
 * implemented. This class extends Observable and notifies every Observer when
 * there is a change in a feed or a feed is added.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public class JeedModel extends Observable {
    private static Logger logger = Logger.getLogger("jeedreader");
    
    private Vector<Feed> feeds;

    /**
     * Instantiates the feeds Vector.
     *
     */
    public JeedModel() {
        this.feeds = new Vector<Feed>();
    }

    /**
     * Adds a feed to the feeds Vector and notifies observers.
     *
     * @param feed The feed to add.
     */
    public void addFeed(Feed feed) {
        feeds.add(feed);
        setChanged();
        notifyObservers(feed);
    }

    /**
     * Updates the supplied feed. Checks for new feed content on-line and adds
     * every item that doesn't already exist in the supplied feed.
     *
     * @param feed The feed to update.
     * @return True if the feed was changed, false otherwise.
     * @exception IOException If the feed origin wasn't accessible.
     * @exception JDOMException If the feed origin wasn't parsable.
     */
    public boolean updateFeed(Feed feed) throws IOException, JDOMException {
        boolean isChanged = false;

        List<FeedItem> newItems = new ArrayList<FeedItem>();
        URL feedLink = feed.getFeedLink();
        Feed updateFeed = FeedUtil.makeFeed(feedLink);
        List<FeedItem> items = feed.getItems();
        List<FeedItem> updateItems = updateFeed.getItems();
        // List to store new items that only exist in updateItems.

        // TODO, should check dates, and HttpHeader to reduce search time?
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
        
        // Notify attached observers
        setChanged();
        // TODO remove and use a constant instead of "newItems"
        notifyObservers("newItems");
        // Could be used If updateFeeds() wants to know if a change occur ed
        return isChanged;
    }

    /**
     * Updates all feeds by calling updateFeed(Feed) for every feed in the
     * Vector feeds stored in this class.
     *
     * @exception IOException If the feed origin wasn't accessible.
     * @exception JDOMException If the feed origin wasn't parsable.
     */
    public void updateFeeds() throws IOException, JDOMException {
        for (Feed feed : feeds) {
            updateFeed(feed);
        }
    }

    /**
     * Returns every item in the supplied Feed.
     *
     * TODO - is this better than having the feed call the feeds methods?
     *
     * @param feed The feed to get items from.
     * @return An array with FeedItems.
     */
    public FeedItem[] getItemsForFeed(Feed feed) {
        return feed.getItems().toArray(new FeedItem[0]);
    }

    /**
     * Returns a Vector with all feeds.
     *
     * @return all feeds stored in this class.
     */
    
    public Vector<Feed> getFeeds() {
        return feeds;
    }

    /**
     * Remove the supplied Feed.
     *
     * @param feed The Feed to remove.
     */
    public void removeFeed(Feed feed) {
        feeds.remove(feed);
    }
}
