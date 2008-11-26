/*
 * @(#)JeedItem.java
 * Time-stamp: "2008-11-26 21:46:50 anton"
 */

public class JeedItem extends RssItem { 
    private boolean isRead = false;
    
    public JeedItem() {
    }
    
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
        
    }
    
    public boolean isRead() {
        return this.isRead;
    }
}
