/*
 * @(#)JeedConfigWriter.java
 * Time-stamp: "2008-12-01 23:39:02 anton"
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

public final class JeedConfigWriter { 
    public static void saveFeeds(List<Feed> feeds) {
        for (Feed feed : feeds) {
            saveFeed(feed);
        }
    }

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

    public static String makeFileName(Feed feed) {
        String result = feed.getFeedLink().toString();
        result = result.replaceAll("/|\\.|\\?|&|:", "-");
        result += "-feed.xml";
        return result;
    }
}
