/*
 * @(#)RssParser.java
 * Time-stamp: "2008-11-25 15:12:05 anton"
 * TODO - character encoding.
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

public class RssParser {
    private String feedType;
    private String rssVersion;
    private RssChannel rssChannel;
    private static Logger logger = Logger.getLogger("jeedreader");
    
    // For inherited classes
    public RssParser() {}
    
    public RssParser(URL url) throws IOException, JDOMException {
        // Throws. IOException, JDOMException
        parseRss(JdomUtil.loadXml(url));
    }
    
    public RssParser(File file) throws IOException, JDOMException {
        // Throws. IOException, JDOMException
        parseRss(JdomUtil.loadXml(file));
    }

    public void parseRss(Document document) {
        Element rootElement = document.getRootElement();
        
        this.rssVersion = rootElement.getAttribute("version").getValue();
        this.feedType = rootElement.getName();
        logger.info("rssVersion: " + rssVersion);
        Element channelElement = rootElement.getChild("channel");
        parseChannel(channelElement);
    }
    
    private void parseChannel(Element channelElement) {
        String channelTitle = channelElement.getChildTextTrim("title");
        URL channelLink = parseURL(channelElement.getChildTextTrim("link"));
        String channelDescription
            = channelElement.getChildTextTrim("description");
        
        // Create new RssChannel
        this.rssChannel = new RssChannel(feedType, rssVersion, channelTitle,
                                         channelDescription, channelLink);
        
        List<Element> itemElements = channelElement.getChildren("item");
        for (Element item : itemElements) {
            parseItem(item);
        }
    }
    
    private void parseItem(Element itemElement) {
        RssItem item =
            new RssItem(itemElement.getChildTextTrim("title"),
                        itemElement.getChildTextTrim("description"));
        String dateString = itemElement.getChildTextTrim("pubDate");
        Date pubDate;
        // TODO - varify dates on different feeds
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
        rssChannel.addItem(item);
    }

    public RssChannel getRssChannel() {
        return this.rssChannel;
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