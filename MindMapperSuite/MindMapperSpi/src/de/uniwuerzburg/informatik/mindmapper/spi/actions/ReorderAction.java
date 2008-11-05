package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import de.uniwuerzburg.informatik.mindmapper.spi.NodeImpl;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * Change the order of a MindMap Node according to a permutation.
 * @author Christian "blair" Schwartz
 */
public class ReorderAction extends AbstractUndoableAction{

    /**
     * The permutation to restore to in case of an undo.
     */
    protected int[] restorePermutation;

    /**
     * The permutation to change to in case of an redo.
     */
    protected int[] permutation;

    /**
     * The node whose chilren should be reordered.
     */
    protected Node parent;

    /**
     * Create and execute an action to reorder the children of.
     * @param document The document owning the parent.
     * @param parent The parent whose children should be reordered.
     * @param permutation The new order of the children.
     */
    public ReorderAction(DocumentImpl document, Node parent, int[] permutation) {
        super(document);
        this.permutation = permutation;
        this.parent = parent;
        ((NodeImpl)parent).reorder(permutation);

        restorePermutation = new int[permutation.length];
        for(int i = 0; i < permutation.length; i++) {
            restorePermutation[permutation[i]] = i;
        }

        postInit();
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        ((NodeImpl)parent).reorder(permutation);
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        ((NodeImpl)parent).reorder(restorePermutation);
    }

    @Override
    public String getPresentationName() {
        return "Reorder Children";
    }

    @Override
    public String getRedoPresentationName() {
        return "Reorder Children";
    }

    @Override
    public String getUndoPresentationName() {
        return "Reorder Children";
    }
}
