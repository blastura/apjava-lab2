/*
 * @(#)RssParser.java
 * Time-stamp: "2008-11-29 17:57:06 anton"
 */


import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.jdom.*;

public class RssParser implements FeedParser { 
    private static Logger logger = Logger.getLogger("jeedreader");
    
    public RssParser() {}

    public RssFeed parse(Document doc) {
        RssFeed rssFeed = new RssFeed("rss");
        
        Element channelElement = doc.getRootElement().getChild("channel");
        Element rootElement = doc.getRootElement();
        
        String version = rootElement.getAttributeValue("version");
        rssFeed.setVersion(version);

        parseChannel(channelElement, rssFeed);
        return rssFeed;
    }
    
    public void parseChannel(Element channelElement, RssFeed rssFeed) {
        String channelTitle = channelElement.getChildTextTrim("title");
        rssFeed.setTitle(channelTitle);
        
        // URL channelLink = FeedUtil.parseURL(channelElement.getChildTextTrim("link"));
        URL channelLink = null;
        String urlString = channelElement.getChildTextTrim("link");
        try {
            if (urlString != null) {
                logger.info("Url String : " + urlString);
                channelLink = new URL(urlString);
                rssFeed.setLink(channelLink);
            }
        } catch (MalformedURLException e) {
            //TODO - fix error message            
            logger.warning("Malformed or missing url in link element, URL: "
                           + urlString);
        }

        String channelDescription
            = channelElement.getChildTextTrim("description");
        rssFeed.setDescribtion(channelDescription);
        
        List<Element> itemElements = channelElement.getChildren("item");
        for (Element item : itemElements) {
            RssItem rssItem = new RssItem();
            parseItem(item, rssItem);
            rssFeed.addItem(rssItem);
        }
    }
    
    public void parseItem(Element itemElement, RssItem item) {
        // TODO - fix parseItem to rssparser
        String title = itemElement.getChildTextTrim("title");
        item.setTitle(title);
        
        String desc = itemElement.getChildTextTrim("description");
        item.setDescribtion(desc);
        
        //         String dateString = itemElement.getChildTextTrim("pubDate");
        //         Date pubDate;
        // TODO - verify dates on different formats
        //         try {
        //             //                         Mon, 17 Nov 2008 14:06:36 GMT
        //             //                         Thu, 20 Nov 2008 18:03:46 +0100
        //             SimpleDateFormat df
        //                 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        //             pubDate = df.parse(dateString);
        //             logger.info("dateString: " + dateString + "\n"
        //                       + "pubDate: " + pubDate);
        //             item.setPubDate(pubDate);
        //         } catch (ParseException e) {
        //             logger.warning("There was an error parsing date: " + dateString);
        //             pubDate = null;
        //         }
    }
}
