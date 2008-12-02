/*
 * @(#)JeedConfigParser.java
 * Time-stamp: "2008-12-01 12:13:13 anton"
 */

import org.jdom.Document;
import org.jdom.Element;
import java.net.URL;
import java.net.MalformedURLException;

public class JeedConfigParser extends RssParser { 
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

    
    
    @Override
    public void parseItem(Element itemElement, RssItem rssItem) {
        boolean isRead = new Boolean(itemElement.getAttributeValue("isRead"));
        rssItem.setIsRead(isRead);
        super.parseItem(itemElement, rssItem);
    }
}