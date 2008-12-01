/*
 * @(#)JeedConfigWriter.java
 * Time-stamp: "2008-11-30 20:39:35 anton"
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

public class JeedConfigWriter { 
    FeedOutputter feedOutputter;
    
    public JeedConfigWriter() {
        feedOutputter = new FeedOutputter();
    }

    public void saveFeeds(List<Feed> feeds) {
        for (Feed feed : feeds) {
            saveFeed(feed);
        }
    }

    public void saveFeed(Feed feed) {
        String fileName = makeFileName(feed);
        
        try {
            File confFile = new File(JeedReader.CONF_DIR, fileName);
            
            Writer fileOut =
                new OutputStreamWriter(new FileOutputStream(confFile), "UTF-8");
            feedOutputter.output(feed, fileOut);
        } catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public String makeFileName(Feed feed) {
        String result = feed.getFeedLink().toString();
        result = result.replaceAll("/|\\.|\\?|&|:", "-");
        result += "-feed.xml";
        return result;
    }
}
