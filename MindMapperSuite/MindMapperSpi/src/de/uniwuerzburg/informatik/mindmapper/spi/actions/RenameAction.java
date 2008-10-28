/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author blair
 */
public class RenameAction extends AbstractUndoableAction{
    protected String newName;
    protected String oldName;
    protected Node node;

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
