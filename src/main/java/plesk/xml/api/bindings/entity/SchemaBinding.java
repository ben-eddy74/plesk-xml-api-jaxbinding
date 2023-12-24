package plesk.xml.api.bindings.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class SchemaBinding {
    
    @XmlAttribute(name = "schemaLocation")
    public String schemafile;


    @XmlElement(name = "bindings", namespace = "https://jakarta.ee/xml/ns/jaxb")
    public List<NodeBinding> nodebindings = new ArrayList<>();
}
