/*
 * @(#)FeedOutputter.java
 * Time-stamp: "2008-11-25 17:49:03 anton"
 */

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class FeedOutputter {
    public FeedOutputter() {
    }

    public void output(FeedChannel channel, OutputStream out) {
        Element rootElement = new Element(channel.getType());
        // TODO - Not for atom, only namespaces?
        rootElement.setAttribute("version", channel.getVersion());
        
        // Channel
        Element channelElement = new Element("channel");
        rootElement.addContent(channelElement);

        // Channel tags
        Element titleElement = new Element("title");
        titleElement.setText(channel.getTitle());
        channelElement.addContent(titleElement);
        
        Element linkElement = new Element("link");
        linkElement.setText(channel.getLink().toString());
        channelElement.addContent(linkElement);
                
        Element descElement = new Element("description");
        descElement.setText(channel.getDescribtion());
        channelElement.addContent(descElement);

        // Items
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

        // Create and output doc
        Document doc = new Document(rootElement);
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, out);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }
}