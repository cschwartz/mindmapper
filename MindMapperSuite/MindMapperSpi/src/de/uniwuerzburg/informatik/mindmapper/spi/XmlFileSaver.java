package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import java.io.IOException;
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
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Element;

/**
 * A saver for XML MindMaps.
 * @author blair
 */
public class XmlFileSaver implements XmlFileStorage{
    /**
     * A document builder used to construct the dom tree.
     */
    protected DocumentBuilder builder;

    /**
     * Create a new saver.
     * @throws ParserConfigurationException If the parser configuration fails.
     */
    public XmlFileSaver() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        builder = documentBuilderFactory.newDocumentBuilder();
    }

    /**
     * Save the document in the file object.
     * @param document The document to save.
     * @param fileObject The file object to save in.
     */
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
