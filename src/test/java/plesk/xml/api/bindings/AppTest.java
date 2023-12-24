package plesk.xml.api.bindings;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.List;

import org.junit.Test;

import plesk.xml.api.bindings.entity.NodeBinding;
import plesk.xml.api.bindings.entity.PropertyBinding;
import plesk.xml.api.bindings.entity.RootBindings;
import plesk.xml.api.bindings.entity.SchemaBinding;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     * @throws JAXBException
     */
    @Test
    public void shouldAnswerWithTrue() throws JAXBException
    {

        RootBindings root = new RootBindings();
        root.version = "3.0";

        SchemaBinding schemabinding = new SchemaBinding();
        schemabinding.schemafile = "test.xsd";
        root.schemabindings.add(schemabinding);

        NodeBinding nodebinding = new NodeBinding();
        nodebinding.node = "//xs:ComplexType";
        schemabinding.nodebindings.add(nodebinding);

        PropertyBinding property = new PropertyBinding();
        property.propertyname = "operations";
        nodebinding.bindings = List.of(property);

        JAXBContext context = JAXBContext.newInstance(RootBindings.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter result = new StringWriter();
        marshaller.marshal(root, result);
        System.out.println(result.toString());
        assertTrue( true );
    }
}
