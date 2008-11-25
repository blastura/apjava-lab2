/*
 * @(#)FeedChannel.java
 * Time-stamp: "2008-11-25 15:37:47 anton"
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

abstract public class FeedChannel { 
    private List<FeedItem> items;
    private String description;
    private String type;
    private String title;
    private String version;
    private URL link;
    
    public FeedChannel(String type, String version,
                       String title, String description, URL link) {
        this.type = type;
        this.version = version;
        this.title = title;
        this.description = description;
        this.link = link;        
        this.items = new ArrayList<FeedItem>();
    }

    public String getType() {
        return this.type;
    }

    public String getVersion() {
        return this.version;
    }

    public void addItem(FeedItem feedItem) {
        items.add(feedItem);
    }

    public List<FeedItem> getItems() {
        return this.items;
    }

    @Override
    public String toString() {
        return "Feed type: " + type + "\n"
            + "Title: " + title + ", Rss-version: " + version + "\n"
            + "Description: " + description + "\n"
            + "Link: " + link + "\n"
            + "Items: " + items + "\n";
    }
}
