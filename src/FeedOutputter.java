/*
 * @(#)FeedOutputter.java
 * Time-stamp: "2008-12-02 23:46:49 anton"
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * FeedOutputter has static methods to output feeds. And a private method to
 * make a feed from an org.jdom.Document instance.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public final class FeedOutputter {
    
    /**
     * Converts the supplied Feed to an org.jdom.Document and prints it on the
     * supplied OutputStream.
     *
     * @param feed The Feed to output.
     * @param out The OutputStream to print the Feed on.
     */
    public static void output(Feed feed, OutputStream out) {
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
    
    /**
     * Converts the supplied Feed to an org.jdom.Document and prints it to the
     * supplied Writer.
     *
     * @param feed The Feed to output.
     * @param out The Writer to print the Feed to.
     */
    public static void output(Feed feed, Writer out) {
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
    
    /**
     * Converts the supplied instance of Feed to an org.jdom.Document. The
     * root-element of the Document will be named "jss", these Feeds are used to
     * store application Feeds to disk.
     *
     * TODO - Not all fields in FeedItem and Feed are used. This should
     * be changed in future versions.
     *
     * @param feed The Feed to make a new Document from.
     * @return The newly created Document.
     */
    private static Document makeDoc(Feed feed) {
        // Element rootElement = new Element(feed.getType());
        Element rootElement = new Element("jss");

        
        // TODO - Not for atom, only namespaces?
        rootElement.setAttribute("version", feed.getVersion());
        
        // TODO - only for jss feeds, but that's all I'm writing?
        rootElement.setAttribute("feedLink", feed.getFeedLink().toString());
        
        // Channel
        Element channelElement = new Element("channel");
        rootElement.addContent(channelElement);

        // Channel tags
        Element titleElement = new Element("title");
        titleElement.setText(feed.getTitle());
        channelElement.addContent(titleElement);
        
        URL linkURL = feed.getLink();
        if (linkURL != null) {
            Element linkElement = new Element("link");
            linkElement.setText(linkURL.toString());
            channelElement.addContent(linkElement);
        }
                
        Element descElement = new Element("description");
        descElement.setText(feed.getDescribtion());
        channelElement.addContent(descElement);

        // Items
        List<FeedItem> items = feed.getItems();
        for (FeedItem item : items) {
            Element itemElement = new Element("item");
            // TODO - isRead
            boolean isRead = item.isRead();
            
            itemElement.setAttribute("isRead", "" + isRead);
            channelElement.addContent(itemElement);
            
            Element itemTitleElement = new Element("title");
            itemTitleElement.setText(item.getTitle());
            itemElement.addContent(itemTitleElement);
            
            Element itemDescElement = new Element("description");
            itemDescElement.setText(item.getDescribtion());
            itemElement.addContent(itemDescElement);
            
            
            URL itemUrl = item.getUrl();
            if (linkURL != null) {
                Element itemLinkElement = new Element("link");
                itemLinkElement.setText(itemUrl.toString());
                itemElement.addContent(itemLinkElement);
            }
        }
        // Create and return a Document
        return new Document(rootElement);
    }
}