package de.uniwuerzburg.informatik.mindmapper.editorapi;

import org.openide.awt.UndoRedo;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;

/**
 * A Cookie providing several services for a Document.
 * @author blair
 */
public interface DocumentCookie extends Cookie{
    /**
     * Returns a Node representing the MindMap Document.
     * @return The node representing the document.
     */
    public Node getDocumentNode();

    /**
     * Returns if the Document has been modified.
     * @return True, if the document has been modified since the last save
     * else, False.
     */
    public boolean isModified();

    /**
     * Returns the Documents UndoRedo.Manager to be shared by all editors.
     * @return The Documents UndoRedo.Manager.
     */
    public UndoRedo.Manager getUndoRedoManager();
}
