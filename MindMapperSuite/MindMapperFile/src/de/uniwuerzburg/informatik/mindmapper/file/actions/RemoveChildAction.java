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
public class RemoveChildAction extends AbstractUndoableAction{

    protected Node parent;
    protected Node childToRemove;
    
    public RemoveChildAction(DataObject dataObject, Node parent, Node childToRemove) {
        super(dataObject);
        this.parent = parent;
        this.childToRemove = childToRemove;
        parent.removeChild(childToRemove);
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        parent.addChild(childToRemove);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
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
