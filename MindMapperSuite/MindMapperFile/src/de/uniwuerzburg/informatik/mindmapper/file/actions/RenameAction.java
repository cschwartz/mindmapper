/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file.actions;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.loaders.DataObject;

/**
 *
 * @author blair
 */
public class RenameAction extends AbstractUndoableAction{
    protected String newName;
    protected String oldName;
    protected Node node;

    public RenameAction(DataObject dataObject, Node node, String newName) {
        super(dataObject);
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
