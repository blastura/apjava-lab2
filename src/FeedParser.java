/*
 * @(#)FeedParser.java
 * Time-stamp: "2008-12-02 22:56:54 anton"
 * TODO - character encoding.
 */

/**
 * Describe interface <code>FeedParser</code> here.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public interface FeedParser {
    /**
     * Parses a supplied org.jdom.Document and returns a Feed.
     *
     * @param doc The Document to parse.
     * @return A newly created Feed containing information from the supplied
     * Document.
     */
    public Feed parse(org.jdom.Document doc);
}