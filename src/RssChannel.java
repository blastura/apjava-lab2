/*
 * @(#)RssChannel.java
 * Time-stamp: "2008-11-25 15:08:51 anton"
 */

import java.net.URL;

public class RssChannel extends FeedChannel { 
    
    public RssChannel(String feedType, String version, String title,
                      String description, URL link) {
        super(feedType, version, title, description, link);
    }
}
