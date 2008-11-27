/*
 * @(#)FeedParser.java
 * Time-stamp: "2008-11-27 16:48:28 anton"
 * TODO - character encoding.
 */

import org.jdom.*;

public interface FeedParser {
    public Feed parse(Document doc);
}