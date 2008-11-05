package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.MindMapFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.filesystems.FileObject;

/**
 * An implementation of the MindMapFactory interface, registered in the
 * META-INF/services system.
 * @author Christian "blair" Schwartz
 */
public class MindMapFactoryImpl implements MindMapFactory{
    /**
     * The loader for xml mindmaps.
     */
    protected XmlFileLoader loader;

    /**
     * The saver for xml mindmaps.
     */
    protected XmlFileSaver saver;

    /**
     * Create a new instance of the factory.
     */
    public MindMapFactoryImpl() {
        try {
            loader = new XmlFileLoader();
            saver = new XmlFileSaver();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MindMapFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create a new MindMap Document.
     * @return The new document.
     */
    public Document createDocument() {
        Document document = new DocumentImpl();
        document.setName("New Document");
        return document;
    }

    /**
     * Load a xml mindmap file.
     * @param fileObject The xml file.
     * @return The loaded document or null if the document couldn't be loaded.
     */
    public Document loadDocument(FileObject fileObject) {
        return loader.loadDocument(fileObject);
    }

    /**
     * Save the document in a xml file.
     * @param fileObject The file to save.
     * @param document The document to save to.
     */
    public void saveDocument(FileObject fileObject, Document document) {
        saver.saveDocument(document, fileObject);
    }
}
