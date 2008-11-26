/*
 * @(#)$FeedOutputterTest.java
 * Time-stamp: "2008-11-26 20:49:13 anton"
 */

import junit.framework.*;
import org.jdom.Document;
import java.io.File;
import java.io.IOException;
import org.jdom.JDOMException;
import org.jdom.Element;

public class FeedOutputterTest extends TestCase {
    public FeedOutputterTest(String name) {
        super(name);
    }

    public void testFeedOutputter() throws IOException, JDOMException {
        Document doc = FeedUtil.loadXml(new File("resources/test2.xml"));
        FeedParser feedParser = FeedUtil.getFeedParser(doc);
        FeedChannel channel = feedParser.parse(doc);
        
        FeedOutputter op = new FeedOutputter();
        op.output(channel, System.out);
    }
}
