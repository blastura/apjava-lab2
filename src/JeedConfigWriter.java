/*
 * @(#)JeedConfigWriter.java
 * Time-stamp: "2008-11-29 17:46:54 anton"
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class JeedConfigWriter { 
    FeedOutputter feedOutputter;
    
    public JeedConfigWriter() {
        feedOutputter = new FeedOutputter();
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
