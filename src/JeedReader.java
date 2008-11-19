/*
 * @(#)JeedReader.java
 * Time-stamp: "2008-11-19 00:16:52 anton"
 */

import java.io.File;

public class JeedReader {
    public JeedReader() {
        RSSParser r = new RSSParser(new File("resources/test2.xml"));
    }

    public static void main(String[] args) {
        new JeedReader();
    }
}
