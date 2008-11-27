/*
 * @(#)$JeedReaderTest.java
 * Time-stamp: "2008-11-27 18:35:08 anton"
 */

import junit.framework.*;
import java.io.File;
import java.util.logging.Logger;

public class JeedReaderTest extends TestCase { 
    private static Logger logger = Logger.getLogger("jeedreader");
    
    public JeedReaderTest(String name) {
        super(name);
    }

    public void testFeedFiles() {
        System.out.println("Path to CONF_DIR: "
                           + JeedReader.CONF_DIR.getAbsolutePath());
        JeedReader jr = new JeedReader();
        File[] feedFiles = jr.getFeedFiles();
        for (int i = 0; i < feedFiles.length; i++) {            
            logger.info("" + feedFiles[i]);
        }
    }
}
