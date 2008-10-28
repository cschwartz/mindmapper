/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.spi.*;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.actions.AbstractUndoableAction;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author blair
 */
public class AppendChildAction extends AbstractUndoableAction{

    protected Node parent;
    protected Node child;

    public AppendChildAction(DocumentImpl document, Node parent, Node child) {
        super(document);
        this.parent = parent;
        this.child = child;
        ((NodeImpl)child).setDocument(document);
        parent.addChild(child);

        postInit();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        parent.removeChild(child);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        parent.addChild(child);
    }

    @Override
    public String getPresentationName() {
        return "Append Child";
    }

    @Override
    public String getUndoPresentationName() {
        return "Append Child";
    }

    @Override
    public String getRedoPresentationName() {
        return "Append Child";
    }

}
