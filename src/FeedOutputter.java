/*
 * @(#)FeedOutputter.java
 * Time-stamp: "2008-11-27 20:45:50 anton"
 */

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.io.OutputStream;

public class FeedOutputter {
    
    public FeedOutputter() {
    }
    
    public void output(Feed feed, OutputStream out) {
        Document doc = makeDoc(feed);
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, out);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }
    
     public void output(Feed feed, Writer out) {
        Document doc = makeDoc(feed);
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, out);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }
    
    private Document makeDoc(Feed feed) {
        Element rootElement = new Element(feed.getType());
        // Element rootElement = new Element("jss");

        
        // TODO - Not for atom, only namespaces?
        rootElement.setAttribute("version", feed.getVersion());
        
        // TODO - only for jss feeds, but thats all I'm writing?
        rootElement.setAttribute("feedLink", feed.getFeedLink().toString());
        
        // Channel
        Element channelElement = new Element("channel");
        rootElement.addContent(channelElement);

        // Channel tags
        Element titleElement = new Element("title");
        titleElement.setText(feed.getTitle());
        channelElement.addContent(titleElement);
        
        Element linkElement = new Element("link");
        linkElement.setText(feed.getLink().toString());
        channelElement.addContent(linkElement);
                
        Element descElement = new Element("description");
        descElement.setText(feed.getDescribtion());
        channelElement.addContent(descElement);

        // Items
        List<FeedItem> items = feed.getItems();
        for (FeedItem item : items) {
            Element itemElement = new Element("item");
            // TODO - isRead
            boolean isRead = false;
            if (item instanceof JeedItem) {
                isRead = ((JeedItem) item).isRead();
            }
            
            itemElement.setAttribute("isRead", "" + isRead);
            channelElement.addContent(itemElement);
            
            Element itemTitleElement = new Element("title");
            itemTitleElement.setText(item.getTitle());
            itemElement.addContent(itemTitleElement);
            
            Element itemDescElement = new Element("description");
            itemDescElement.setText(item.getDescribtion());
            itemElement.addContent(itemDescElement);
        }
        
        // Create doc
        return new Document(rootElement);
    }
}