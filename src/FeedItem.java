/*
 * @(#)FeedItem.java
 * Time-stamp: "2008-11-25 16:11:14 anton"
 */

abstract public class FeedItem {
    private String title;
    private String description;
    private boolean isRead;
    
    public FeedItem(String title, String description) {
        this.title = title;
        this.description = description;
        this.isRead = false;
    }

    public boolean isRead() {
        return this.isRead;   
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getDescribtion() {
        return this.description;
    }
    
    @Override
    public String toString() {
        return "Title: " + title + "\n"
            + "Description: " + description + "\n";
    }
}
