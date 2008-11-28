/*
 * @(#)JeedModel.java
 * Time-stamp: "2008-11-28 23:58:46 anton"
 */

import java.util.Vector;
import java.util.logging.Logger;

public class JeedModel { 
    private static Logger logger = Logger.getLogger("jeedreader");
    private Vector<Feed> feeds;

    public JeedModel() {
        this.feeds = new Vector<Feed>();
    }
    
    public void addFeed(Feed feed) {
        feeds.add(feed);
    }

    public void updateFeed(Feed oldFeed, Feed newFeed) {
        // TODO
    }

    public FeedItem[] getItemsForFeed(Feed feed) {
        return feed.getItems().toArray(new FeedItem[0]);
    }
    
    // TODO is this really the way to go????
    public String getDescribtionFromFeedItem(FeedItem feedItem) {
        return feedItem.getDescribtion();
    }
    
    public Vector<Feed> getFeeds() {
        return feeds;
    }

    public void removeFeed(Feed feed) {
        feeds.remove(feed);
    }
}
