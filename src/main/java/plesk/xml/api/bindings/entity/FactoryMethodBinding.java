package plesk.xml.api.bindings.entity;

import jakarta.xml.bind.annotation.XmlAttribute;

public class FactoryMethodBinding {
    
    @XmlAttribute(name = "name")
    public String factoryMethodName;
}
