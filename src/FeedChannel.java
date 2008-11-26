/*
 * @(#)FeedChannel.java
 * Time-stamp: "2008-11-26 21:58:20 anton"
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

abstract public class FeedChannel<T> { 
    private List<T> items;
    private String description;
    private String type;
    private String title;
    private String version;
    private URL link;
    
    public FeedChannel(String type) {
        this.type = type;
        this.items = new ArrayList<T>();
    }

    public String getTitle() {
        return this.title;
    }

    public URL getLink() {
        return this.link;
    }
    
    public String getDescribtion() {
        return this.description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(URL link) {
        this.link = link;
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
    
    public void addItem(T feedItem) {
        items.add(feedItem);
    }

    public List<T> getItems() {
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
