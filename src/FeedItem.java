/*
 * @(#)FeedItem.java
 * Time-stamp: "2008-12-02 23:50:09 anton"
 */

import java.net.URL;

/**
 * This class is meant to be extended by different FeedItem-types. A reference
 * to this type is used directly in the application.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
abstract public class FeedItem {
    private String title;
    private String description;
    private boolean isRead = false;
    private URL url;

    public FeedItem() {
    }
    
    /**
     * Creates a new <code>FeedItem</code> instance. The title and description
     * are required.
     *
     * @param title The title of this FeedItem.
     * @param description The description of this FeedItem.
     */
    public FeedItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Used to change if this FeedItem is read or not.
     *
     * @param isRead True indicates that this FeedItem is read, false means
     * unread.
     */
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * Used to check if this FeedItem is read.
     *
     * @return True means that this FeedItem is read, false means unread.
     */
    public boolean isRead() {
        return this.isRead;
    }

    /**
     * Get the title of this FeedItem.
     *
     * @return The title of this FeedItem.
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * Set the title of this FeedItem.
     *
     * @param title The new title of this FeedItem.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the description of this FeedItem.
     *
     * @return The description of this FeedItem.
     */
    public String getDescribtion() {
        return this.description;
    }

    /**
     * Set the description of this FeedItem.
     *
     * @param description The new description of this FeedItem.
     */
    public void setDescribtion(String description) {
        this.description = description;
    }

    /**
     * Get the URL of this FeedItem.
     *
     * @return The URL of this FeedItem.
     */
    public URL getUrl() {
        return this.url;
    }

    /**
     * Set the URL of this FeedItem.
     *
     * @param url The new URL of this FeedItem.
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * Return a String representation of this FeedItem. If the FeedItem is read
     * "(Ny) + title" will be returned. If it's not read only the title with be
     * returned.
     *
     * @return The String representation of this FeedItem. If read, "(Ny) +
     * title" otherwise only the title.
     */
    @Override
    public String toString() {
        return isRead() ? title : "(Ny) " + title;
    }

    /**
     * Get a html description of this FeedItem. This is used by JeedView to
     * display items in an JEditorPane.
     *
     * @return A String containing a html description of this FeedItem. Title in
     * <h1 /> tags, description in a <p /> tag and URL in an <a href"URL"/> tag.
     */
    public String getHtmlDescription() {
        String htmlString = "";
        if (this.title != null) {
            htmlString += "<h1>" + this.title + "</h1>";
        }
        if (this.description != null) {
            htmlString += "\n<p>" + this.description + "</p>";
        }
        if (this.url != null) {
            htmlString += "\n<p><a href=\"" + this.url + "\">" + this.url + "</a></p>";
        }
        return htmlString;
    }

    /**
     * Compare this object to others. This is used when updating feeds.
     *
     * @param obj An object to compare this FeedItem with.
     * @return True if both string and title matches a supplied FeedItem, false
     * otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FeedItem) {
            FeedItem compareItem = (FeedItem) obj;
            // TODO NullPointerException?
            return (this.title.equals(compareItem.getTitle())
                    && this.description.equals(compareItem.getDescribtion()));
        } else {
            return false;
        }
    }
}
