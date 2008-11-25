/*
 * @(#)JeedConfigParser.java
 * Time-stamp: "2008-11-25 08:12:08 anton"
 */

import java.io.File;
import org.jdom.Document;
import java.io.FilenameFilter;

public class JeedConfigParser extends RssParser { 
    // private Document doc;
    private static final File CONF_DIR = new File("config");
    
    public JeedConfigParser() {
        //         try {
        //             this.doc = JdomUtil.loadXml(new File(FEEDS_FILE));
        //         }
        //         catch (IOException e) {
        //             e.printStackTrace();
        //         } catch (JDOMException e) {
        //             e.printStackTrace();
        //         }
        //         super.parseRss(doc);
    }

    /**
     * Returns an array containing all files in CONF_DIR which end with
     * feed.xml, these files should contain saved feeds.
     *
     * @return An array with feed files.
     */
    public File[] getFeedFiles() {
        System.out.println(CONF_DIR.getAbsolutePath());
        if (CONF_DIR.isDirectory()) {
            File[] feedFiles = CONF_DIR.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (CONF_DIR.equals(dir)
                                && name.matches(".*feed.xml"));
                    }
                });
            return feedFiles;
        } else {
            return null;
        }
    }
}