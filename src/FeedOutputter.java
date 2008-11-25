/*
 * @(#)FeedOutputter.java
 * Time-stamp: "2008-11-25 16:20:12 anton"
 */

import org.jdom.*;
import org.jdom.output.XMLOutputter;
import java.io.IOException;

import java.util.List;

public class FeedOutputter {
    
    public FeedOutputter(FeedChannel channel) {
        Element rootElement = new Element(channel.getType());
        // TODO - Not for atom, only namespaces?
        rootElement.setAttribute("version", channel.getVersion());
        
        Element channelElement = new Element("channel");
        rootElement.addContent(channelElement);
        
        List<FeedItem> items = channel.getItems();
        for (FeedItem item : items) {
            Element itemElement = new Element("item");
            // TODO - isRead
            itemElement.setAttribute("isRead", "" + item.isRead());
            channelElement.addContent(itemElement);
            
            Element itemTitleElement = new Element("title");
            itemTitleElement.setText(item.getTitle());
            itemElement.addContent(itemTitleElement);
            
            Element itemDescElement = new Element("description");
            itemDescElement.setText(item.getDescribtion());
            itemElement.addContent(itemDescElement);
        }
        
        Document doc = new Document(rootElement);
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(doc, System.out);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }        
    }
}