package plesk.xml.api.bindings.entity;

import jakarta.xml.bind.annotation.XmlAttribute;

public class PropertyBinding {
    
    @XmlAttribute(name = "name")
    public String propertyname;
}
