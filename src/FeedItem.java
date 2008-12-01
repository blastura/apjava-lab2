/*
 * @(#)FeedItem.java
 * Time-stamp: "2008-11-30 20:14:31 anton"
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
        return isRead() ? title : "(Ny) " + title;
    }

    public String getInfoString() {
        return "Title: " + title + "\n"
            + "Description: " + description + "\n";
    }
    
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
