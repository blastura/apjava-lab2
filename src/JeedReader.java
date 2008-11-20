/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-11-19 23:09:04 anton"
 */

import java.io.File;
import java.io.IOException;
import org.jdom.JDOMException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URL;

public class JeedReader {
    private static Logger logger = Logger.getLogger("jeedreader");

    public JeedReader() {
        try {
            RssParser parser =
                new RssParser(new URL("http://sydsvenskan.se/senastenytt/?context=senasteNyttRss"));
            System.out.println(parser.getRssChannel());
        }
        catch (IOException e) {
            // TODO - File not found.
            e.printStackTrace();
        }
        catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Logger.getLogger("jeedreader").setLevel(Level.INFO);
        new JeedReader();
    }
}
