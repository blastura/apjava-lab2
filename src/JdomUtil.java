/*
 * @(#)JdomUtil.java
 * Time-stamp: "2008-11-24 08:16:51 anton"
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class JdomUtil {
    public JdomUtil() {
    }
    
    /**
     * TODO - add correct author or somehting
     * Laddar en XML-fil och returnerar ett DOM-dokument.
     * Se http://www.jdom.org/ för mer info.
     * @param srcURL XML-fil med kartan.
     * @return DOM Document
     * @throws IOException
     * @throws JDOMException
     *
     */
    public static Document loadXml(URL srcURL) throws IOException,
                                                      JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(srcURL);
        return doc;
    }

    /**
     * TODO - add correct author or somehting
     * Laddar en XML-fil och returnerar ett DOM-dokument.
     * Se http://www.jdom.org/ för mer info.
     * @param srcFile XML-fil med kartan.
     * @return DOM Document
     * @throws IOException
     * @throws JDOMException
     *
     */
    public static Document loadXml(File srcFile) throws IOException,
                                                        JDOMException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(srcFile);
        return doc;
    }
}
