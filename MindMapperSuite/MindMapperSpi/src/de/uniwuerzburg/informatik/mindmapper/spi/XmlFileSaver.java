/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Element;

/**
 *
 * @author blair
 */
public class XmlFileSaver implements XmlFileStorage{
    
    protected DocumentBuilder builder;
    
    public XmlFileSaver() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        builder = documentBuilderFactory.newDocumentBuilder();
    }

    void saveDocument(Document document, FileObject fileObject) {
        FileLock lock = new FileLock();
        try {
            org.w3c.dom.Document xmlDocument = builder.newDocument();
            xmlDocument.getDocumentElement().setAttributeNS(xmlNamespace, rootNameAttribute, document.getName());

            Element root = xmlDocument.createElementNS(xmlNamespace, rootElement);
            root.setAttributeNS(xmlNamespace, rootNameAttribute, document.getName());
            xmlDocument.getDocumentElement().appendChild(root);
            
            Source source = new DOMSource(xmlDocument);
            Result result = new StreamResult(fileObject.getOutputStream(lock));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
        } catch (IOException ex) {
            Logger.getLogger(XmlFileSaver.class.getName()).log(Level.SEVERE, null, ex);
        } catch(TransformerException ex) {
            Logger.getLogger(XmlFileSaver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.releaseLock();
        }
    }
}
