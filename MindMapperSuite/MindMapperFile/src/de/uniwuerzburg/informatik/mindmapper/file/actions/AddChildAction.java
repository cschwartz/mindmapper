/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.file.actions;

import de.uniwuerzburg.informatik.mindmapper.api.MindMapFactory;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.util.Lookup;


public class AddChildAction extends AbstractUndoableEdit{

    protected Node parent;
    protected Node newChild;
    
    public AddChildAction(Node parent) {
        this.parent = parent;
        MindMapFactory factory = Lookup.getDefault().lookup(de.uniwuerzburg.informatik.mindmapper.api.MindMapFactory.class);
        newChild = factory.createNode();
        newChild.setName("New Node");
        parent.addChild(newChild);
    }

    public AddChildAction(Node parent, Node child) {
        this.parent = parent;
        this.newChild = child;
        parent.addChild(child);
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

