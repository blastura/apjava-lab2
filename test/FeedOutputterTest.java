/*
 * @(#)$FeedOutputterTest.java
 * Time-stamp: "2008-11-27 20:35:43 anton"
 */

import junit.framework.*;
import org.jdom.Document;
import java.io.File;
import java.io.IOException;
import org.jdom.JDOMException;
import java.util.logging.Logger;

public class FeedOutputterTest extends TestCase {
    private static Logger logger = Logger.getLogger("jeedreader");
    
    public FeedOutputterTest(String name) {
        super(name);
    }

    public void testFeedOutputter() throws IOException, JDOMException {
        logger.info("In testFeedOutputter");
        
        //FeedChannel channel = new FeedUtil().makeFeed("resources/test2.xml");
        //FeedOutputter op = new FeedOutputter();
        //op.output(channel, System.out);        
    }
}
