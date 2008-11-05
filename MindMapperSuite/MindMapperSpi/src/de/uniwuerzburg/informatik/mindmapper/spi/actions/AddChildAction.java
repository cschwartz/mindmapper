package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.spi.NodeImpl;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.DocumentImpl;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * An action to add a new child to a node.
 * @author Christian "blair" Schwartz
 */
public class AddChildAction extends AbstractUndoableAction{

    /**
     * The parent to add the child to.
     */
    protected Node parent;

    /**
     * The new child of the parent.
     */
    protected NodeImpl newChild;

    /**
     * Create and execute an action which adds a new MindMap node to the parent
     * node in the document.
     * @param document The document hosting the node.
     * @param parent The parent to add a node to.
     */
    public AddChildAction(DocumentImpl document, Node parent) {
        super(document);
        this.parent = parent;
        newChild = new NodeImpl();
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