/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file.actions;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author blair
 */
public class RenameAction extends AbstractUndoableEdit{
    protected String newName;
    protected String oldName;
    protected Node node;

    public RenameAction(Node node, String newName) {
        this.newName = newName;
        this.oldName = node.getName();
        this.node = node;
        node.setName(newName);
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
