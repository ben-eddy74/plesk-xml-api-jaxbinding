package plesk.xml.api.bindings.entity;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class NodeBinding {
    
    @XmlAttribute(name = "node")
    public String node;

    
    @XmlElement(name = "property", namespace = "https://jakarta.ee/xml/ns/jaxb")
    public List<PropertyBinding> propertybindings;
    
    @XmlElement(name = "factoryMethod", namespace = "https://jakarta.ee/xml/ns/jaxb")
    public List<FactoryMethodBinding> factorymethodsbindings;
    
    @XmlElement(name = "class", namespace = "https://jakarta.ee/xml/ns/jaxb")
    public List<ClassBinding> classbindings;
}
