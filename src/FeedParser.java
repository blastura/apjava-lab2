/*
 * @(#)FeedParser.java
 * Time-stamp: "2008-11-26 19:14:24 anton"
 * TODO - character encoding.
 */

import org.jdom.*;

public interface FeedParser {
    public FeedChannel parse(Document doc);
}