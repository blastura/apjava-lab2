/*
 * @(#)$FeedOutputterTest.java
 * Time-stamp: "2008-11-25 17:31:01 anton"
 */

import junit.framework.*;

public class FeedOutputterTest extends TestCase {
    public FeedOutputterTest(String name) {
        super(name);
    }

    public void testFeedOutputter() {
        JeedReader jr = new JeedReader();
        FeedOutputter op = new FeedOutputter();
        op.output(jr.getRssParser().getRssChannel(), System.out);
    }
}
