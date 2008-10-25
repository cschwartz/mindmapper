/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.editorapi;

import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author blair
 */
public interface MindMapEditorFactory{
    public CloneableTopComponent createMindMapEditor(MultiDataObject documentNode);
}
