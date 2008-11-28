/*
 * @(#)FeedItem.java
 * Time-stamp: "2008-11-28 23:39:17 anton"
 */

abstract public class FeedItem {
    private String title;
    private String description;
    private boolean isRead = false;

    public FeedItem() {
    }
    
    public FeedItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    public boolean isRead() {
        return this.isRead;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescribtion() {
        return this.description;
    }

    public void setDescribtion(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return title;
    }

    public String getInfoString() {
        return "Title: " + title + "\n"
            + "Description: " + description + "\n";
    }
}
