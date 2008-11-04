/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import de.uniwuerzburg.informatik.mindmapper.spi.NodeImpl;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author blair
 */
public class ReorderAction extends AbstractUndoableAction{

    protected int[] restorePermutation;
    protected int[] permutation;
    protected Node parent;
    
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
