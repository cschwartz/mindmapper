package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * An undoable action on DocumentImpl instances.
 * @author Christian "blair" Schwartz
 */
public abstract class AbstractUndoableAction extends AbstractUndoableEdit{
    /**
     * The document to perform the action on.
     */
    protected DocumentImpl document;

    /**
     * The modified status of the document before the action.
     */
    protected boolean oldModified;

    /**
     * The modified status of the document after the action.
     */
    protected boolean modified;

    /**
     * Create a new undoable action to perform on the document.
     * @param document The document to perform the action on.
     */
    public AbstractUndoableAction(DocumentImpl document) {
        super();
        this.document = document;
        oldModified = document.isModified();
        modified = true;
    }

    /**
     * Register the action at the UndoRedo.Manager and update the modified
     * status of the document.
     * Call at the end of the constructor.
     */
    protected void postInit() {
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
