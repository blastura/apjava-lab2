/*
 * @(#)RSSParser.java
 * Time-stamp: "2008-11-19 00:16:01 anton"
 */

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
    
public class RSSParser {
    public RSSParser(File file) {
        Document doc;
        try {
            // Throws. IOException, JDOMException
            doc = loadXml(file);
            List<Element> itemElements
                = ((Element) doc.getRootElement().getChildren().get(0)).getChildren("item");
            for (Element element : itemElements) {
                System.out.println(element.getName());
            }
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        } catch (JDOMException e) {
            // TODO
            e.printStackTrace();
        }
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