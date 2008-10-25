/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Link;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
 *
 * @author blair
 */
public class XmlFileLoader implements XmlFileStorage{
    protected SchemaFactory schemaFactory;
    protected DocumentBuilder builder;
    protected Map<String, de.uniwuerzburg.informatik.mindmapper.api.Node> idMap;
    
    public XmlFileLoader() throws ParserConfigurationException {
        schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        builder = documentBuilderFactory.newDocumentBuilder();
        
        idMap = new HashMap<String, de.uniwuerzburg.informatik.mindmapper.api.Node>();
    }
    
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

    protected boolean parseLinks(Document document, Element element) {
         NodeList links = element.getChildNodes();
         for(int i = 0; i < links.getLength(); i++) {
             Node node = links.item(i);
             if(node.getNodeType() == Node.ELEMENT_NODE) {
                 if(node.getNamespaceURI().equals(xmlNamespace) &&
                         node.getLocalName().equals(linkElement)) {
                     Element linkElement = (Element)node;
                     Link newLink = document.addLink();
                     de.uniwuerzburg.informatik.mindmapper.api.Node sourceNode = null;
                     de.uniwuerzburg.informatik.mindmapper.api.Node targetNode = null;
                     sourceNode = idMap.get(linkElement.getAttribute(linkSourceAttribute));
                     targetNode = idMap.get(linkElement.getAttribute(linkTargetAttribute));
                     if(targetNode != null && sourceNode != null) {
                         newLink.setSource(sourceNode);
                         newLink.setTarget(targetNode);
                     } else
                         return false;
                     String name = linkElement.getAttribute(linkNameAttribute);
                     newLink.setName(name);
                 }
             }
         }
         return true;
    }
}
