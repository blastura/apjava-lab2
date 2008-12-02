/*
 * @(#)Feed.java
 * Time-stamp: "2008-12-02 23:30:51 anton"
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is meant to be extended by different FeedItem-types. A reference
 * to this type is used directly in the application.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
abstract public class Feed { 
    private List<FeedItem> items;
    private String description;
    private String type;
    private String title;
    private String version;
    private URL link;
    private URL feedLink;
    private boolean isRead = false;
    
    /**
     * Creates a new <code>Feed</code> instance. A String defining type is
     * required. // TODO change this behavior
     *
     * @param type A string defining the type off this feed. It should reflect
     * the root-tag of the Feed Document. That is most of the time, "rss" should
     * be supplied.
     */
    public Feed(String type) {
        this.type = type;
        this.items = new ArrayList<FeedItem>();
    }

    /**
     * Get the Title of this Feed.
     *
     * @return The Title of this Feed.
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the Link of this Feed.
     *
     * @return The Link of this Feed.
     */
    public URL getLink() {
        return this.link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    /**
     * Get the Feedlink of this Feed.
     *
     * @return The Feedlink of this Feed.
     */
    public URL getFeedLink() {
        return this.feedLink;
    }

    public void setFeedLink(URL feedLink) {
        this.feedLink = feedLink;
    }
    
    /**
     * Get the Description of this Feed.
     *
     * @return The Description of this Feed.
     */
    public String getDescribtion() {
        return this.description;
    }

    public void setDescribtion(String description) {
        this.description = description;
    }
    
    /**
     * Get the Type of this Feed.
     *
     * @return The Type of this Feed.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get the Version of this Feed.
     *
     * @return The Version of this Feed.
     */
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public void addItem(FeedItem feedItem) {
        items.add(feedItem);
    }

    /**
     * Get the Isread of this Feed.
     *
     * @return The Isread of this Feed.
     */
    public boolean getIsRead() {
        return this.isRead;
    }
    
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * Get the FeedItems in this Feed.
     *
     * @return The FeedItems in this Feed.
     */
    public List<FeedItem> getItems() {
        return this.items;
    }

    /**
     * Return a String representation of this Feed.
     *
     * @return The title of this feed.
     */
    @Override
    public String toString() {
        return title;
    }
    
    
    /**
     * Get a String representation of this Feed.
     *
     * @return A string describing this Feed is returned. The string contains,
     * type, title, version, description, link and all the items.
     */
    public String getInfoString() {
        return "Feed type: " + type + "\n"
            + "Title: " + title + ", Rss-version: " + version + "\n"
            + "Description: " + description + "\n"
            + "Link: " + link + "\n"
            + "Items: " + items + "\n";
    }
}
