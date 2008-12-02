/*
 * @(#)RssFeed.java
 * Time-stamp: "2008-12-03 00:15:27 anton"
 */

/**
 * Extends Feed and should provide RSS specific behavior. TODO - extend when
 * needed.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public class RssFeed extends Feed { 
    
    /**
     * Creates a new <code>RssFeed</code> instance.
     *
     * @param feedType A String describing the type of this Feed. Should be
     * "rss". TODO - This behaviour should be changed.
     */
    public RssFeed(String feedType) {
        super(feedType);
    }
}
