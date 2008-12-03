/*
 * @(#)JeedConfigWriter.java
 * Time-stamp: "2008-12-03 01:28:23 anton"
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

/**
 * JeedConfigWriter has methods to save a Feed and to create a filename for it.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public final class JeedConfigWriter { 
    public static void saveFeeds(List<Feed> feeds) {
        for (Feed feed : feeds) {
            saveFeed(feed);
        }
    }

    /**
     * Save the supplied Feed, in JeedReader.CONF_DIR, directory to disk.
     *
     * @param feed The Feed to save.
     */
    public static void saveFeed(Feed feed) {
        String fileName = makeFileName(feed);
        
        try {
            File confFile = new File(JeedReader.CONF_DIR, fileName);
            
            Writer fileOut =
                new OutputStreamWriter(new FileOutputStream(confFile), "UTF-8");
            FeedOutputter.output(feed, fileOut);
        } catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO
            e.printStackTrace();
        }
    }

    /**
     * Create a filename for the supplied Feed. The FeedLink is used as a name,
     * some characters are replaced to '-' and "feed.xml" is appended.
     *
     * @param feed The Feed to create a filename for.
     * @return The newly created filename.
     */
    public static String makeFileName(Feed feed) {
        String result = feed.getFeedLink().toString();
        result = result.replaceAll("/|\\.|\\?|&|:|~", "-");
        result += "-feed.xml";
        return result;
    }
}
