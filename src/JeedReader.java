/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-11-26 19:06:43 anton"
 */

import java.io.File;
import java.io.IOException;
import org.jdom.JDOMException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URL;
import org.jdom.Document;

public class JeedReader {
    private static Logger logger = Logger.getLogger("jeedreader");
    private FeedParser feedParser;
    
    public JeedReader() {
        try {
            //FeedParser parser =
            //    new FeedParser(new URL("http://sydsvenskan.se/senastenytt/?context=senasteNyttRss"));
            Document doc = FeedUtil.loadXml(new File("resources/test2.xml"));
            this.feedParser = FeedUtil.getFeedParser(doc);
            FeedChannel channel = feedParser.parse(doc);
        } catch (IOException e) {
            // TODO - File not found.
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        
        // Create GUI
        // new JeedGui();
    }

    public FeedParser getFeedParser() {
        return this.feedParser;
    }
    
    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
}
