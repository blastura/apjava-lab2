/*
 * @(#)RssChannel.java
 * Time-stamp: "2008-11-19 21:48:51 anton"
 */

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class RssChannel { 
    private String version;
    private String title;
    private String description;
    private URL link;
    private List<RssItem> items;
    
    public RssChannel(String version, String title,
                      String description, URL link) {
        this.version = version;
        this.title = title;
        this.description = description;
        this.link = link;
        this.items = new ArrayList<RssItem>();
    }

    public void addItem(RssItem rssItem) {
        items.add(rssItem);
    }
    
    @Override
    public String toString() {
        return "Title: " + title + ", Rss-version: " + version + "\n"
            + "Description: " + description + "\n"
            + "Link: " + link + "\n"
            + "Items: " + items;
    }
}
