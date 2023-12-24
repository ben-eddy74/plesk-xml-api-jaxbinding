package plesk.xml.api.bindings;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
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
import plesk.xml.api.bindings.entity.NodeBinding;
import plesk.xml.api.bindings.entity.PropertyBinding;
import plesk.xml.api.bindings.entity.RootBindings;
import plesk.xml.api.bindings.entity.SchemaBinding;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException, PropertyException, JAXBException {
        System.out.println("Hello World!");

        DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
        docfactory.setNamespaceAware(true);

        XPathFactory xpathfactory = XPathFactory.newDefaultInstance();
        XPath xpath = xpathfactory.newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
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
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public Iterator<String> getPrefixes(String namespaceURI) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });
        String xsdfolder = "C:\\Temp\\Plesk\\1.6.9.1\\";
        String expression = "//xs:complexType[xs:choice/xs:element[@name='add']]";

        RootBindings root = new RootBindings();
        root.version = "3.0";

        Files.list(Paths.get(xsdfolder)).forEach(path -> {

            if (Files.isDirectory(path)) {
                return;
            }

            try {
                Document doc = docfactory.newDocumentBuilder().parse(path.toFile());

                NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);

                SchemaBinding schemabinding = new SchemaBinding();
                schemabinding.schemafile = path.getFileName().toString();

                System.out.println(nodeList.getLength());
                for (int i = 0; i < nodeList.getLength(); i++) {

                    if (nodeList.item(i).getAttributes().getLength() > 0) {
                        NodeBinding nodebinding = new NodeBinding();
                        nodebinding.node = "//%s[@name='%s']/xs:choice".formatted(
                                nodeList.item(i).getNodeName(),
                                nodeList.item(i).getAttributes().item(0).getTextContent()
                        );
                        schemabinding.nodebindings.add(nodebinding);

                        PropertyBinding property = new PropertyBinding();
                        property.propertyname = "operations";
                        nodebinding.bindings = List.of(property);
                    }
                }

                if(!schemabinding.nodebindings.isEmpty()){
                    root.schemabindings.add(schemabinding);
                }
            } catch (SAXException | IOException | XPathExpressionException | ParserConfigurationException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JAXBContext context = JAXBContext.newInstance(RootBindings.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter result = new StringWriter();
        marshaller.marshal(root, result);
        System.out.println(result.toString());

    }
}
