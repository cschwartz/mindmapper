/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.spi.NodeImpl;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


public class AddChildAction extends AbstractUndoableAction{

    protected Node parent;
    protected NodeImpl newChild;
    
    public AddChildAction(DocumentImpl document, Node parent) {
        super(document);
        this.parent = parent;
        newChild = new NodeImpl();
        newChild.setDocument(document);
        newChild.setName("New Node");
        parent.addChild(newChild);

        postInit();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        parent.removeChild(newChild);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        parent.addChild(newChild);
    }

    @Override
    public String getPresentationName() {
        return "Add Child";
    }

    @Override
    public String getUndoPresentationName() {
        return "Add Child";
    }

    @Override
    public String getRedoPresentationName() {
        return "Add Child";
    }

}

