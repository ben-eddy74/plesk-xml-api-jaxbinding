package plesk.xml.api.bindings.control;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Eddy Vermoen <@ben-eddy74>
 */
public class DocManager {

    DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
    XPathFactory xpathfactory = XPathFactory.newDefaultInstance();
    XPath xpath;

    public DocManager() {

        docfactory.setNamespaceAware(true);

        this.xpath = xpathfactory.newXPath();
        this.xpath.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                return switch (prefix) {
                    case "jaxb" ->
                        "https://jakarta.ee/xml/ns/jaxb";
                    case "xs" ->
                        "http://www.w3.org/2001/XMLSchema";
                    default ->
                        "";
                };
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });
    }

    public Document fromFile(Path path) {
        try {
            return docfactory.newDocumentBuilder().parse(path.toFile());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DocManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public NodeList fromXPath(Document doc, String expression){
        try {
            return (NodeList) this.xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(DocManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public <T> String Stringify(T clazz){
        try {
            JAXBContext context = JAXBContext.newInstance(clazz.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter result = new StringWriter();
            marshaller.marshal(clazz, result);
            return result.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(DocManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
