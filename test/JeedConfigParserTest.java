/*
 * @(#)$JeedConfigParserTest.java
 * Time-stamp: "2008-11-25 08:10:42 anton"
 */

import junit.framework.*;
import java.io.File;

public class JeedConfigParserTest extends TestCase { 
    public JeedConfigParserTest(String name) {
        super(name);
    }

    public void testFeedFiles() {
        JeedConfigParser jp = new JeedConfigParser();
        File[] feedFiles = jp.getFeedFiles();
        for (int i = 0; i < feedFiles.length; i++) {            
            System.out.println(feedFiles[i]);
        }
    }
}