package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * An action to rename a MindMap Node.
 * @author Christian "blair" Schwartz
 */
public class RenameAction extends AbstractUndoableAction{
    /**
     * The new name of the MindMap Node.
     */
    protected String newName;

    /**
     * The old name of the MindMap Node.
     */
    protected String oldName;

    /**
     * The node whose name will be changed.
     */
    protected Node node;

    /**
     * Create and execute an action which will change the name of the MindMap
     * Node node to newName.
     * @param document The document owning the node.
     * @param node The node whose name to change.
     * @param newName The new name of the node.
     */
    public RenameAction(DocumentImpl document, Node node, String newName) {
        super(document);
        this.newName = newName;
        this.oldName = node.getName();
        this.node = node;
        node.setName(newName);

        postInit();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        node.setName(oldName);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        node.setName(newName);
    }
}
