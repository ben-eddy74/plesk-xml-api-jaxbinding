package plesk.xml.api.bindings.entity;

import jakarta.xml.bind.annotation.XmlAttribute;

public class ClassBinding {
    
    @XmlAttribute(name = "name")
    public String className;
}
