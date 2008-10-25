/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.editor;

import de.uniwuerzburg.informatik.mindmapper.editorapi.MindMapEditorFactory;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author blair
 */
public class MindMapEditorFactoryImpl implements MindMapEditorFactory{

    public CloneableTopComponent createMindMapEditor(MultiDataObject dataObject) {
        return new MindMapEditor(dataObject);
    }

}
