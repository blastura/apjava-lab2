/*
 * @(#)RssItem.java
 * Time-stamp: "2008-11-28 23:49:27 anton"
 */

import java.util.Date;

public class RssItem extends FeedItem { 
    private Date pubDate;
    
    public RssItem() {
    }
    
    /**
     * Title and Description are required in RSS 2.0.
     */
    public RssItem(String title, String description) {
        super(title, description);
    }
    
    /**
     * Get the publication date of this item.
     *
     * @return The publication date of this item.
     */
    public Date getPubDate() {
        return pubDate;
    }
    
    /**
     * Set the publication date of this item.
     *
     * @param newPubDate The new publication date of this item.
     */
    public void setPubDate(final Date newPubDate) {
        this.pubDate = newPubDate;
    }

    @Override
    public String getInfoString() {
        return super.toString() + "\n"
            + "PubDate: " + pubDate + "\n";
    }
}
