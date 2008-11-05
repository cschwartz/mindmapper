package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Link;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Loader for Xml MindMap files.
 * @author Christian "blair" Schwartz
 */
public class XmlFileLoader implements XmlFileStorage{
    /**
     * A schema factory to validate the document before loading.
     */
    protected SchemaFactory schemaFactory;

    /**
     * A builder for the documents DOM tree.
     */
    protected DocumentBuilder builder;

    /**
     * A map between node id and node instance.
     */
    protected Map<String, de.uniwuerzburg.informatik.mindmapper.api.Node> idMap;

    /**
     * Create a new file loader
     * @throws javax.xml.parsers.ParserConfigurationException In case the
     * parser configuration fails.
     */
    public XmlFileLoader() throws ParserConfigurationException {
        schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        builder = documentBuilderFactory.newDocumentBuilder();
        
        idMap = new HashMap<String, de.uniwuerzburg.informatik.mindmapper.api.Node>();
    }

    /**
     * Load the document in the fileObject.
     * @param fileObject The fileObject containing the document.
     * @return The loaded document.
     */
    public Document loadDocument(FileObject fileObject) {
        try {
            if (!validateDocument(fileObject.getInputStream())) {
                return null;
            }
            org.w3c.dom.Document xmlDocument = builder.parse(fileObject.getInputStream());
            
            Document document = parseDocument(xmlDocument.getDocumentElement());

            return document;
        } catch(IOException e) {
            Logger.getLogger(MindMapFactoryImpl.class.getName()).log(Level.SEVERE, null, e);
        } catch(SAXException e) {
            Logger.getLogger(MindMapFactoryImpl.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            idMap.clear();
        }
        
        return null;
    }

    /**
     * Parse the document element.
     * @param documentElement The document element to parse.
     * @return The loaded document.
     */
    protected Document parseDocument(Element documentElement) {
        Document document = new DocumentImpl();
        String documentName = documentElement.getAttribute(rootNameAttribute);
        document.setName(documentName);
        
        NodeList childNodes = documentElement.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if(child.getNodeType() == Node.ELEMENT_NODE) {
                if(child.getNamespaceURI().equals(xmlNamespace) &&
                        child.getLocalName().equals(rootElement)) {
                    parseNode(document.getRootNode(), (Element)child);
                } else if(child.getNamespaceURI().equals(xmlNamespace) &&
                        child.getLocalName().equals("Links")) {
                    if(!parseLinks(document, (Element) child))
                        return null;
                }
            }
        }
        return document;
    }

    /**
     * Validate the document given by the input stream.
     * @param inputStream The input stream of the document to parse.
     * @return True, if the document is valid, else False.
     */
    protected boolean validateDocument(InputStream inputStream) {
        try {
            Schema schema = schemaFactory.newSchema(new File("MindMapperSpi/src/de/uniwuerzburg/informatik/mindmapper/spi/MindMapperSchema.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(inputStream));
        } catch (SAXException e) {
            return false;
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Parse the node element.
     * @param parent The parent to add the node to.
     * @param xmlElement The xml element to parse.
     */
    protected void parseNode(de.uniwuerzburg.informatik.mindmapper.api.Node parent, Element xmlElement) {
        String id = xmlElement.getAttribute(childIdAttribute);
        String name = xmlElement.getAttribute(childNameAttribute);
        parent.setName(name);
        idMap.put(id, parent);
        
        NodeList children = xmlElement.getChildNodes();
        for(int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if(child.getNodeType() == Node.ELEMENT_NODE) {
                if(child.getNamespaceURI().equals(xmlNamespace) &&
                        child.getLocalName().equals(childElement)) {
                    NodeImpl newChild = new NodeImpl();
                    parent.addChild(newChild);
                    parseNode(newChild, (Element)child);
                }
            }
        }
    }

    /**
     * Parse the links element.
     * @param document The document to add the links to.
     * @param element The links element to parse.
     * @return True, if all node ids could be matched, else False.
     */
    protected boolean parseLinks(Document document, Element element) {
         NodeList links = element.getChildNodes();
         for(int i = 0; i < links.getLength(); i++) {
             Node node = links.item(i);
             if(node.getNodeType() == Node.ELEMENT_NODE) {
                 if(node.getNamespaceURI().equals(xmlNamespace) &&
                         node.getLocalName().equals(linkElement)) {
                     Element link = (Element)node;
                     Link newLink = document.addLink();
                     de.uniwuerzburg.informatik.mindmapper.api.Node sourceNode = null;
                     de.uniwuerzburg.informatik.mindmapper.api.Node targetNode = null;
                     sourceNode = idMap.get(link.getAttribute(linkSourceAttribute));
                     targetNode = idMap.get(link.getAttribute(linkTargetAttribute));
                     if(targetNode != null && sourceNode != null) {
                         newLink.setSource(sourceNode);
                         newLink.setTarget(targetNode);
                     } else
                         return false;
                     String name = link.getAttribute(linkNameAttribute);
                     newLink.setName(name);
                 }
             }
         }
         return true;
    }
}
