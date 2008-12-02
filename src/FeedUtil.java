/*
 * @(#)FeedUtil.java
 * Time-stamp: "2008-12-03 00:12:19 anton"
 */

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * FeedUtil provides static methods to be used by the JeedReader application.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public final class FeedUtil {
    private static Logger logger = Logger.getLogger("jeedreader");

    /**
     * Returns an array containing all files in CONF_DIR which end with
     * feed.xml, these files should contain saved feeds.
     *
     * @return An array with feed files.
     */
    public static File[] getFeedFiles() {
        if (JeedReader.CONF_DIR.isDirectory()) {
            File[] feedFiles
                = JeedReader.CONF_DIR.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return (JeedReader.CONF_DIR.equals(dir)
                                    && name.matches(".*feed.xml"));
                        }
                    });
            return feedFiles;
        } else {
            return null;
        }
    }

    /**
     * Creates a Feed from the supplied File.
     *
     * @param file The File to create a new Feed from.
     * @return a <code>Feed</code> value
     * @exception IllegalArgumentException If the supplied Document is not of
     * type RSS 2.0. TODO - Should use validation instead.
     */
    public static Feed makeFeed(File file) throws IllegalArgumentException {
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

    /**
     * Makes an URL of this supplied string and creates a Feed it.
     *
     * @param urlString An URL String locationg a Syndication Feed.
     * @return A newly created Feed.
     * @exception MalformedURLException Thrown to indicate that a malformed URL
     * has occurred. Either no legal protocol could be found in a specification
     * string or the string could not be parsed.
     * @exception IOException If the URL isn't pointing to an accessable
     * resource.
     * @exception JDOMException If the URL isn't pointing to a resource thats
     * parsable by JDOM.
     * @exception IllegalArgumentException If the supplied Document is not of
     * type RSS 2.0. TODO - Should use validation instead.
     */
    public static Feed makeFeed(String urlString) throws MalformedURLException,
                                                         IOException,
                                                         JDOMException,
                                                         IllegalArgumentException {
        URL url = new URL(urlString);
        return makeFeed(url);
    }

    /**
     * Creates a Feed from the supplied URL.
     *
     * @param url The URL should point to a supported Syndication Feed type.
     * @return A newly created Feed.
     * @exception IOException If the URL isn't pointing to an accessable
     * resource.
     * @exception JDOMException If the URL isn't pointing to a resource thats
     * parsable by JDOM.
     * @exception IllegalArgumentException If the supplied Document is not of
     * type RSS 2.0. TODO - Should use validation instead.
     */
    public static Feed makeFeed(URL url) throws IOException,
                                                JDOMException,
                                                IllegalArgumentException {
        Document doc = loadXml(url);
        FeedParser feedParser = getFeedParser(doc);
        Feed feed = feedParser.parse(doc);
        feed.setFeedLink(url);
        return feed;
    }

    /**
     * Creates and returns a suitable implementation of FeedParser, depending on
     * the supplied Document.
     *
     * @param doc The document to get a suitable FeedParser for.
     * @return A newly created FeedParser.
     * @exception IllegalArgumentException If the supplied Document is not of
     * type RSS 2.0. TODO - Should use validation instead.
     */
    private static FeedParser getFeedParser(Document doc)
        throws IllegalArgumentException {
        Element rootElement = doc.getRootElement();
        String feedType = rootElement.getName();

        if (feedType.equalsIgnoreCase("rss")) {
            // TODO Rss versions
            String feedVersion = rootElement.getAttribute("version").getValue();
            if (!feedVersion.equals("2.0")) {
                logger.warning("Rss version other than 2.0 detected");
                throw new IllegalArgumentException("Rss versions other than "
                                                   + "2.0 is not supported.");
            }
            return new RssParser();
        } else if (feedType.equalsIgnoreCase("jss")) {
            return new JeedConfigParser();
        } else {
            // TODO if feed version differ etc
            System.err.println("Not an rss feed");
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
    private static Document loadXml(File srcFile) throws IOException,
                                                         JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(srcFile);
        return doc;
    }
}
