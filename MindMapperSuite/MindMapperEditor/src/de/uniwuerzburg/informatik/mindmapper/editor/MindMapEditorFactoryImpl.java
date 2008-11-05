package de.uniwuerzburg.informatik.mindmapper.editor;

import de.uniwuerzburg.informatik.mindmapper.editorapi.MindMapEditorFactory;
import org.openide.loaders.MultiDataObject;
import org.openide.windows.CloneableTopComponent;

/**
 * An implementation for the MindMapEditorFactory interface, registered via
 * the META-INF/services system.
 * @author Christian "blair" Schwartz
 */
public class MindMapEditorFactoryImpl implements MindMapEditorFactory{

    /**
     * Creates a new Editor for the MindMap described by the dataObject.
     * @param dataObject A dataObject containing a DocumentCookie.
     * @return The created editor.
     */
    public CloneableTopComponent createMindMapEditor(MultiDataObject dataObject) {
        return new MindMapEditor(dataObject);
    }

}
