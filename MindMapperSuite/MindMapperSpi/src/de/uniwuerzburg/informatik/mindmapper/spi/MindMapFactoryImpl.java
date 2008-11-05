/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.MindMapFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.openide.filesystems.FileObject;

/**
 *
 * @author blair
 */
public class MindMapFactoryImpl implements MindMapFactory{

    protected XmlFileLoader loader;
    protected XmlFileSaver saver;

    public MindMapFactoryImpl() {
        try {
            loader = new XmlFileLoader();
            saver = new XmlFileSaver();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MindMapFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Document createDocument() {
        Document document = new DocumentImpl();
        document.setName("New Document");
        return document;
    }
    
    public Document loadDocument(FileObject fileObject) {
        return loader.loadDocument(fileObject);
    }

    public void saveDocument(FileObject fileObject, Document document) {
        saver.saveDocument(document, fileObject);
    }

    

}
