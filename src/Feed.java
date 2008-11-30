/*
 * @(#)Feed.java
 * Time-stamp: "2008-11-30 18:06:07 anton"
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

abstract public class Feed { 
    private List<FeedItem> items;
    private String description;
    private String type;
    private String title;
    private String version;
    private URL link;
    private URL feedLink;
    private boolean isRead = false;
    
    public Feed(String type) {
        this.type = type;
        this.items = new ArrayList<FeedItem>();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getLink() {
        return this.link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public URL getFeedLink() {
        return this.feedLink;
    }

    public void setFeedLink(URL feedLink) {
        this.feedLink = feedLink;
    }
    
    public String getDescribtion() {
        return this.description;
    }

    public void setDescribtion(String description) {
        this.description = description;
    }
    
    public String getType() {
        return this.type;
    }

    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public void addItem(FeedItem feedItem) {
        items.add(feedItem);
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public List<FeedItem> getItems() {
        return this.items;
    }

    @Override
    public String toString() {
        return title;
    }
    
    
    public String getInfoString() {
        return "Feed type: " + type + "\n"
            + "Title: " + title + ", Rss-version: " + version + "\n"
            + "Description: " + description + "\n"
            + "Link: " + link + "\n"
            + "Items: " + items + "\n";
    }
}
