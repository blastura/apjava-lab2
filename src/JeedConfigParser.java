/*
 * @(#)JeedConfigParser.java
 * Time-stamp: "2008-12-02 23:08:27 anton"
 */

import org.jdom.Document;
import org.jdom.Element;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Extends RssParser and overrides some methods to parse attributes and tags
 * contained in the format saved by this application.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public class JeedConfigParser extends RssParser { 
    /**
     * Parses the root-element of the supplied Document. Creates an RssFeed with
     * supplied type "jss".
     *
     * @param doc a <code>Document</code> value
     * @return a <code>RssFeed</code> value
     */
    @Override
    public RssFeed parse(Document doc) {
        RssFeed rssFeed = new RssFeed("jss");
        
        Element channelElement = doc.getRootElement().getChild("channel");
        Element rootElement = doc.getRootElement();
        
        String version = rootElement.getAttributeValue("version");
        rssFeed.setVersion(version);
        
        //URL feedLink = FeedUtil.parseURL(rootElement.getAttributeValue("feedLink"));
        URL feedLink = null;
        try {
            feedLink = new URL(rootElement.getAttributeValue("feedLink"));
        } catch (MalformedURLException e) {
            //TODO - fix error message
            System.err.println("Malformed url: "
                               + rootElement.getAttributeValue("feedLink"));
            System.err.println("Doc baseURI: " + doc.getBaseURI());
            e.printStackTrace();
        }
        rssFeed.setFeedLink(feedLink);
        
        parseChannel(channelElement, rssFeed);
        return rssFeed;
    }
    
    /**
     * Parses an item-element, reads and handles the attribute "isRead" and then
     * calls super.parseItem(...).
     *
     * @param itemElement The Element to parse.
     * @param rssItem The RssItem to add information to.
     */
    @Override
    public void parseItem(Element itemElement, RssItem rssItem) {
        boolean isRead = new Boolean(itemElement.getAttributeValue("isRead"));
        rssItem.setIsRead(isRead);
        super.parseItem(itemElement, rssItem);
    }
}