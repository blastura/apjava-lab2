/*
 * @(#)RssItem.java
 * Time-stamp: "2008-11-19 22:55:08 anton"
 */

import java.util.Date;

public class RssItem { 
    private String title;
    private String description;
    private Date pubDate;
    
    /**
     * Title and Description are required in RSS 2.0.
     */
    public RssItem(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    /**
     * Get the publication date of this item.
     *
     * @return The publication date of this item.
     */
    public final Date getPubDate() {
        return pubDate;
    }
    
    /**
     * Set the publication date of this item.
     *
     * @param newPubDate The new publication date of this item.
     */
    public final void setPubDate(final Date newPubDate) {
        this.pubDate = newPubDate;
    }
    
    @Override
    public String toString() {
        return "\nTitle: " + title + "\n"
            + "Description: " + description + "\n";
    }
}
