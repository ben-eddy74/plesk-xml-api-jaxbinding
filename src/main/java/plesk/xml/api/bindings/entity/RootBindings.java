package plesk.xml.api.bindings.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bindings", namespace = "https://jakarta.ee/xml/ns/jaxb")
public class RootBindings {
    
    @XmlAttribute(name = "version")
    public String version;

    @XmlElement(name = "bindings", namespace = "https://jakarta.ee/xml/ns/jaxb")
    public List<SchemaBinding> schemabindings = new ArrayList<>();
}
