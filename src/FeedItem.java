/*
 * @(#)FeedItem.java
 * Time-stamp: "2008-11-26 21:46:38 anton"
 */

abstract public class FeedItem {
    private String title;
    private String description;
    
    public FeedItem() {
    }
    
    public FeedItem(String title, String description) {
        this.title = title;
        this.description = description;
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
        return "Title: " + title + "\n"
            + "Description: " + description + "\n";
    }
}
