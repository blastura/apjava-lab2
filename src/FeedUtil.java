/*
 * @(#)FeedUtil.java
 * Time-stamp: "2008-12-01 11:57:24 anton"
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

    public static Feed makeFeed(String urlString) throws MalformedURLException,
                                                         IOException,
                                                         JDOMException {
        URL url = new URL(urlString);
        return makeFeed(url);
    }

    /**
     * Describe <code>makeFeed</code> method here.
     *
     * @param url The URL should point to a supported supped.
     * @return The newly created feed.
     * @exception IOException If the URL isn't pointing to an accessable
     * resource.
     * @exception JDOMException If the URL isn't pointing to a resource thats
     * parsable by JDOM.
     */
    public static Feed makeFeed(URL url) throws IOException, JDOMException {
        Document doc = loadXml(url);
        FeedParser feedParser = getFeedParser(doc);
        Feed feed = feedParser.parse(doc);
        feed.setFeedLink(url);
        return feed;
    }

    private static FeedParser getFeedParser(Document doc)
        throws IOException, JDOMException, IllegalArgumentException {
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
    private static Document loadXml(File srcFile) throws IOException,
                                                         JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(srcFile);
        return doc;
    }
}
