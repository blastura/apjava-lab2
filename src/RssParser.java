/*
 * @(#)RssParser.java
 * Time-stamp: "2008-12-03 00:27:56 anton"
 */


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import org.jdom.*;

/**
 * Parses RSS 2.0 feeds.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public class RssParser implements FeedParser { 
    private static Logger logger = Logger.getLogger("jeedreader");
    
    /**
     * Creates a new <code>RssParser</code> instance.
     *
     */
    public RssParser() {}

    /**
     * Parses the root-element of the supplied Document, and calls
     * parseChannel(channelElement, rssFeed) with the channel-element and a
     * newly created RssFeed.
     *
     * @param doc The Document to parse.
     * @return A newly created RssFeed containing information from the supplied
     * Document.
     */
    public RssFeed parse(Document doc) {
        RssFeed rssFeed = new RssFeed("rss");
        Element channelElement = doc.getRootElement().getChild("channel");
        Element rootElement = doc.getRootElement();
        String version = rootElement.getAttributeValue("version");
        rssFeed.setVersion(version);

        parseChannel(channelElement, rssFeed);
        return rssFeed;
    }
    
    /**
     * Parses the supplied channel Element, and puts the information in the
     * supplied RssFeed.
     *
     * @param channelElement The Element to parse, this should be the <channel/>
     * element in an RSS feed.
     * @param rssFeed The modified parameter RssFeed.
     */
    public void parseChannel(Element channelElement, RssFeed rssFeed) {
        String channelTitle = channelElement.getChildTextTrim("title");
        rssFeed.setTitle(channelTitle);
        
        String urlString = channelElement.getChildTextTrim("link");
        try {
            if (urlString != null) {
                logger.info("Channel link urlString : " + urlString);
                rssFeed.setLink(new URL(urlString));
            }
        } catch (MalformedURLException e) {
            //TODO - fix error message            
            logger.warning("Malformed or missing url in link element, URL: "
                           + urlString);
        }

        String channelDescription
            = channelElement.getChildTextTrim("description");
        rssFeed.setDescribtion(channelDescription);
        
        @SuppressWarnings("unchecked") // JDOM doesn't support generics
        List<Element> itemElements = channelElement.getChildren("item");
        for (Element item : itemElements) {
            RssItem rssItem = new RssItem();
            parseItem(item, rssItem);
            rssFeed.addItem(rssItem);
        }
    }
    
    /**
     * Parses the supplied item Element, and puts the information in the
     * supplied RssItem.
     *
     * @param itemElement The Element to parse, this should be one of the
     * <item/> elements in an RSS feed.
     * @param item The modified parameter RssItem.
     */
    public void parseItem(Element itemElement, RssItem item) {
        // TODO - fix parseItem to rssparser
        String title = itemElement.getChildTextTrim("title");
        item.setTitle(title);
        
        String desc = itemElement.getChildTextTrim("description");
        item.setDescribtion(desc);
        
        String urlString = itemElement.getChildTextTrim("link");
        try {
            if (urlString != null) {
                logger.info("Item urlString : " + urlString);
                item.setUrl(new URL(urlString));
            }
        } catch (MalformedURLException e) {
            //TODO - don't catch throw this Exception
            logger.warning("Malformed or missing url in itemElement element, URL: "
                           + urlString);
        }
        
        //// Parse and insert Dates
        //
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
