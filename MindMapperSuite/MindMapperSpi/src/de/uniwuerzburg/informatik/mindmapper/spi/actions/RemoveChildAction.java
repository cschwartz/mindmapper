package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import de.uniwuerzburg.informatik.mindmapper.spi.NodeImpl;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * An action to remove a child from a MindMap Node.
 * @author Christian "blair" Schwartz
 */
public class RemoveChildAction extends AbstractUndoableAction{
    /**
     * The parent to remove the node from.
     */
    protected Node parent;

    /**
     * The child to remove from the parent.
     */
    protected Node childToRemove;

    /**
     * The index of the node before deletiion.
     */
    protected int oldIndex;

    /**
     * Create and execute an action to remove a child from a parent MindMap Node.
     * @param document The document owning the parent.
     * @param parent The parent to remove a child from.
     * @param childToRemove The child to remove the parent from.
     */
    public RemoveChildAction(DocumentImpl document, Node parent, Node childToRemove) {
        super(document);
        this.parent = parent;
        this.childToRemove = childToRemove;
        for(int i = 0; i < parent.getChildren().length; i++) {
            if(parent.getChildren()[i] == childToRemove) {
                oldIndex = i;
            }
        }
        parent.removeChild(childToRemove);

        postInit();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ((NodeImpl)parent).addChild((NodeImpl)childToRemove, oldIndex);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();

        for(int i = 0; i < parent.getChildren().length; i++) {
            if(parent.getChildren()[i] == childToRemove) {
                oldIndex = i;
            }
        }

        parent.removeChild(childToRemove);
    }

    @Override
    public String getPresentationName() {
        return "Remove Child";
    }

    @Override
    public String getUndoPresentationName() {
        return "Remove Child";
    }

    @Override
    public String getRedoPresentationName() {
        return "Remove Child";
    }

}
