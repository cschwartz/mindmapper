package de.uniwuerzburg.informatik.mindmapper.editorapi;

import org.openide.loaders.MultiDataObject;
import org.openide.windows.CloneableTopComponent;

/**
 * A factory for MindMap editors.
 * @author Christian "blair" Schwartz
 */
public interface MindMapEditorFactory {
    /**
     * Create a new MindMap Editor for the document represented by the given
     * dataObject
     * @param dataObject A dataObject containing a DocumentCookie.
     * @return The created editor.
     */
    public CloneableTopComponent createMindMapEditor(MultiDataObject dataObject);
}
