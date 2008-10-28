package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blair
 */
public abstract class AbstractUndoableAction extends AbstractUndoableEdit{

    protected DocumentImpl document;
    protected boolean oldModified;
    protected boolean modified;

    public AbstractUndoableAction(DocumentImpl document) {
        super();
        this.document = document;
        oldModified = document.isModified();
        modified = true;
    }

    public void postInit() {
        document.getUndoRedoManager().addEdit(this);
        document.setModified(true);
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        document.setModified(oldModified);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        document.setModified(modified);
    }
}
