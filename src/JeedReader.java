/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-11-25 16:27:57 anton"
 */

import java.io.File;
import java.io.IOException;
import org.jdom.JDOMException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URL;

public class JeedReader {
    private static Logger logger = Logger.getLogger("jeedreader");
    private RssParser rssParser;
    
    public JeedReader() {
        try {
            //RssParser parser =
            //    new RssParser(new URL("http://sydsvenskan.se/senastenytt/?context=senasteNyttRss"));
            this.rssParser =
                new RssParser(new File("resources/test2.xml"));
            logger.info(rssParser.getRssChannel().toString());
        } catch (IOException e) {
            // TODO - File not found.
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        
        // Create GUI
        // new JeedGui();
    }

    public RssParser getRssParser() {
        return this.rssParser;
    }
    
    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
}
