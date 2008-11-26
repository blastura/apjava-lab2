/*
 * @(#)FeedUtil.java
 * Time-stamp: "2008-11-26 22:09:11 anton"
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
    
    public FeedUtil() {
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

    
    public static URL parseURL(String urlString) {
        try {
            // Throws: MalformedURLException
            return new URL(urlString);
        } catch (MalformedURLException e) {
            //TODO
            logger.warning("MalformedURLException");
            return null;
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
    public static Document loadXml(URL srcURL) throws IOException,
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
    public static Document loadXml(File srcFile) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(srcFile);
        return doc;
    }
}
