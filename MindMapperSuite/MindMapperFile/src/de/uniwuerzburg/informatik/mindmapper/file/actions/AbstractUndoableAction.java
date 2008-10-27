package de.uniwuerzburg.informatik.mindmapper.file.actions;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.loaders.DataObject;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blair
 */
public abstract class AbstractUndoableAction extends AbstractUndoableEdit{

    protected DataObject dataObject;
    protected boolean oldModified;
    protected boolean modified;

    public AbstractUndoableAction(DataObject dataObject) {
        super();
        this.dataObject = dataObject;
        oldModified = dataObject.isModified();
        modified = true;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        dataObject.setModified(oldModified);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        dataObject.setModified(modified);
    }
}
