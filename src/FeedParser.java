/*
 * @(#)FeedParser.java
 * Time-stamp: "2008-12-01 11:43:48 anton"
 * TODO - character encoding.
 */

public interface FeedParser {
    public Feed parse(org.jdom.Document doc);
}