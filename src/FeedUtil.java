/*
 * @(#)FeedUtil.java
 * Time-stamp: "2008-11-30 01:37:55 anton"
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.Element;
import java.net.MalformedURLException;
import java.util.logging.Logger;

public class FeedUtil {
    private static Logger logger = Logger.getLogger("jeedreader");
    
    public static Feed makeFeed(File file) {
        System.out.println("Path to file: "
                           + file.getAbsolutePath());
        try {
            Document doc = loadXml(file);
            FeedParser feedParser = getFeedParser(doc);
            Feed feed = feedParser.parse(doc);
            return feed;
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            System.exit(-1);
            return null;
        } catch (JDOMException e) {
            // TODO
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
    
    public static Feed makeFeed(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        return makeFeed(url);
    }

    public static Feed makeFeed(URL url) {
        try {
            Document doc = loadXml(url);
            FeedParser feedParser = getFeedParser(doc);
            Feed feed = feedParser.parse(doc);
            feed.setFeedLink(url);
            return feed;
        } catch (IOException e) {
            // TODO
            System.err.println("Error with URL: " + url);
            return null;
        } catch (JDOMException e) {
            // TODO
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
    
    public static FeedParser getFeedParser(Document doc) throws IOException,
                                                                JDOMException {
        Element rootElement = doc.getRootElement();
        String feedType = rootElement.getName();
        
        if (feedType.equalsIgnoreCase("rss")) {
            // TODO Rss versions
            // String feedVersion = rootElement.getAttribute("version").getValue();
            return new RssParser();
        } else if (feedType.equalsIgnoreCase("jss")) {
            return new JeedConfigParser();
        } else {
            // TODO if feed version differ etc
            System.err.println("Not an rss feed");
            return null;
        }
    }

    
    public static boolean isURL(String urlString) {
            // TODO is this ok
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            logger.warning("MalformedURLException from "
                           + "urlString: " + urlString);
            return false;
        }
    }
    
    /**
     * TODO - add correct author or somehting
     * Laddar en XML-fil och returnerar ett DOM-dokument.
     * Se http://www.jdom.org/ för mer info.
     * @param srcURL XML-fil med kartan.
     * @return DOM Document
     * @throws IOException
     * @throws JDOMException
     *
     */
    private static Document loadXml(URL srcURL) throws IOException,
                                                      JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(srcURL);
        return doc;
    }

    /**
     * TODO - add correct author or somehting
     * Laddar en XML-fil och returnerar ett DOM-dokument.
     * Se http://www.jdom.org/ för mer info.
     * @param srcFile XML-fil med kartan.
     * @return DOM Document
     * @throws IOException
     * @throws JDOMException
     *
     */
    private static Document loadXml(File srcFile) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(srcFile);
        return doc;
    }
}
