package plesk.xml.api.bindings;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.PropertyException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import plesk.xml.api.bindings.control.DocManager;
import plesk.xml.api.bindings.entity.ClassBinding;
import plesk.xml.api.bindings.entity.FactoryMethodBinding;
import plesk.xml.api.bindings.entity.NodeBinding;
import plesk.xml.api.bindings.entity.PropertyBinding;
import plesk.xml.api.bindings.entity.RootBindings;
import plesk.xml.api.bindings.entity.SchemaBinding;

public class App {

    public static void main(String[] args) throws IOException, PropertyException, JAXBException {
        System.out.println("Generate JAXB Binding files for Plesk XML API");

        if(args.length != 1){
            System.out.println("Path to folder with Plesk schema files is not provided.");
            System.exit(1);
        }

        String xsdfolder = args[0];
        
        if(!Files.isDirectory(Path.of(xsdfolder))){
            System.out.println("Unable to access folder " + xsdfolder);
            System.exit(2);
        }

        List<String> xsdfiles = List.of("agent_input.xsd", "agent_output.xsd");

        xsdfiles.forEach(f -> {
            if(!Files.exists(Path.of(xsdfolder, f))){
                System.out.println("Unable to find %s in %s".formatted(f, xsdfolder));
                System.exit(3);
            }
        });
        
        DocManager docmgr = new DocManager();

        String includexpath = "//xs:include";
        String choicexpath = "/xs:schema/xs:complexType[xs:choice and not(contains(@name, 'Filter'))]";
        
        for (String xsdfile : xsdfiles) {
            
            System.out.println("Processing " + xsdfile);
            
            Document agent = docmgr.fromFile(Path.of(xsdfolder, xsdfile));
            NodeList imports = docmgr.fromXPath(agent, includexpath);

            RootBindings root = new RootBindings();
            root.version = "3.0";

            // Add binding for agent operators
            SchemaBinding agentschemabinding = new SchemaBinding();
            agentschemabinding.schemafile = xsdfile;
            root.schemabindings.add(agentschemabinding);
            
            NodeBinding agentnodebinding = new NodeBinding();
            if(xsdfile.equals("agent_input.xsd")){
                agentnodebinding.node = "/xs:schema/xs:element/xs:complexType/xs:complexContent/xs:extension/xs:sequence[xs:choice]";
            } else {
                agentnodebinding.node = "/xs:schema/xs:complexType/xs:sequence/xs:choice[xs:choice]";
            }
            agentschemabinding.nodebindings = List.of(agentnodebinding);
            
            PropertyBinding agentoperators = new PropertyBinding();
            agentoperators.propertyname = "operators";
            agentnodebinding.propertybindings = List.of(agentoperators);
            
            // Add bindings for import operations
            for (int i = 0; i < imports.getLength(); i++) {

                String importfile = imports.item(i).getAttributes().getNamedItem("schemaLocation").getNodeValue();
                System.out.println("Processing " + importfile);

                Document doc = docmgr.fromFile(Path.of(xsdfolder, importfile));
                NodeList choiceElements = docmgr.fromXPath(doc, choicexpath);

                SchemaBinding schemabinding = new SchemaBinding();
                schemabinding.schemafile = importfile;

                for (int x = 0; x < choiceElements.getLength(); x++) {

                    if (choiceElements.item(x).getAttributes().getLength() > 0) {
                        System.out.println(choiceElements.item(x).getAttributes().item(0).getTextContent());
                        NodeBinding nodebinding = new NodeBinding();
                        nodebinding.node = "//%s[@name='%s']/xs:choice".formatted(choiceElements.item(x).getNodeName(),
                                choiceElements.item(x).getAttributes().item(0).getTextContent()
                        );
                        schemabinding.nodebindings.add(nodebinding);

                        PropertyBinding property = new PropertyBinding();
                        property.propertyname = "operations";
                        nodebinding.propertybindings = List.of(property);
                    }
                }

                // Fix collision of ProtectedDirLocation
                if(importfile.equals("protected_dir.xsd")){
                    NodeBinding protectedDirLocation = new NodeBinding();
                    protectedDirLocation.node = "//xs:complexType[@name='ProtectedDirLocation']";
                    schemabinding.nodebindings.add(protectedDirLocation);
                    
                    FactoryMethodBinding factoryMethod = new FactoryMethodBinding();
                    factoryMethod.factoryMethodName = "ProtectedDirLocationType";
                    protectedDirLocation.factorymethodsbindings = List.of(factoryMethod);
                }
                
                // Fix collision of InitialSetupType
                if(importfile.equals("server_output.xsd")){
                    NodeBinding initialSetupType = new NodeBinding();
                    initialSetupType.node = "//xs:complexType[@name='InitialSetupType']";
                    schemabinding.nodebindings.add(initialSetupType);
                    
                    ClassBinding classBinding = new ClassBinding();
                    classBinding.className = "InitialServerSetupType";
                    initialSetupType.classbindings = List.of(classBinding);
                }
                
                if (!schemabinding.nodebindings.isEmpty()) {
                    root.schemabindings.add(schemabinding);
                }
            }

            Path outputfile = Path.of(xsdfolder, "%s.xjb".formatted(xsdfile));

            try (FileOutputStream outputstream = new FileOutputStream(outputfile.toString())){
                String xml = docmgr.Stringify(root);
                outputstream.write(xml.getBytes(), 0, xml.length());
                System.out.println("Generated " + outputfile.toString());
            } catch(Exception ex){
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /*
        


        });

        System.out.println(docmgr.Stringify(root));
         */
    }
}
