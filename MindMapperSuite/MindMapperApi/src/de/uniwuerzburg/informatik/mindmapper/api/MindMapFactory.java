/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.api;

import java.net.URL;
import org.openide.filesystems.FileObject;

/**
 *
 * @author blair
 */
public interface MindMapFactory {
    public Document createDocument();
    public Node createNode();
    public Document loadDocument(FileObject fileObject);
    public void saveDocument(FileObject fileObject, Document document);
}
