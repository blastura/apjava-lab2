/*
 * @(#)RssParser.java
 * Time-stamp: "2008-11-26 21:59:16 anton"
 */


import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom.*;
import java.util.logging.Logger;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class RssParser implements FeedParser { 
    private static Logger logger = Logger.getLogger("jeedreader");
    
    public RssParser() {}

    public RssChannel parse(Document doc) {
        RssChannel<RssItem> rssChannel = new RssChannel("rss");
        
        Element channelElement = doc.getRootElement().getChild("channel");
        
        Element rootElement = doc.getRootElement();
        String version = rootElement.getAttributeValue("version");
        rssChannel.setVersion(version);

        parseChannel(channelElement, rssChannel);
        return rssChannel;
    }
    
    private void parseChannel(Element channelElement, RssChannel rssChannel) {
        String channelTitle = channelElement.getChildTextTrim("title");
        rssChannel.setTitle(channelTitle);
        
        URL channelLink = FeedUtil.parseURL(channelElement.getChildTextTrim("link"));
        rssChannel.setLink(channelLink);
        
        String channelDescription
            = channelElement.getChildTextTrim("description");
        rssChannel.setDescribtion(channelDescription);
        
        List<Element> itemElements = channelElement.getChildren("item");
        for (Element item : itemElements) {
            RssItem rssItem = new RssItem();
            parseItem(item, rssItem);
            rssChannel.addItem(rssItem);
        }
    }
    
    public void parseItem(Element itemElement, RssItem item) {
        // TODO - fix parseItem to rssparser
        String title = itemElement.getChildTextTrim("title");
        item.setTitle(title);
        
        String desc = itemElement.getChildTextTrim("description");
        item.setDescribtion(desc);
        
        String dateString = itemElement.getChildTextTrim("pubDate");
        Date pubDate;
        // TODO - verify dates on different formats
        try {
            //                         Mon, 17 Nov 2008 14:06:36 GMT
            //                         Thu, 20 Nov 2008 18:03:46 +0100
            SimpleDateFormat df
                = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            pubDate = df.parse(dateString);
            logger.info("dateString: " + dateString + "\n"
                        + "pubDate: " + pubDate);
            item.setPubDate(pubDate);
        } catch (ParseException e) {
            logger.warning("There was an error parsing date: " + dateString);
            pubDate = null;
        }
    }

    public URL parseURL(String urlString) {
        try {
            // Throws: MalformedURLException
            return new URL(urlString);
        } catch (MalformedURLException e) {
            //TODO
            logger.warning("MalformedURLException");
            return null;
        }
    }
}
