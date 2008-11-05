package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Link;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.spi.actions.AddChildAction;
import de.uniwuerzburg.informatik.mindmapper.spi.actions.AppendChildAction;
import de.uniwuerzburg.informatik.mindmapper.spi.actions.RemoveChildAction;
import de.uniwuerzburg.informatik.mindmapper.spi.actions.RenameAction;
import de.uniwuerzburg.informatik.mindmapper.spi.actions.ReorderAction;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.undo.AbstractUndoableEdit;
import org.openide.awt.UndoRedo;
import org.openide.awt.UndoRedo.Manager;

/**
 * An implementation of a MindMap Document.
 * @author Christian "blair" Schwartz
 */
public class DocumentImpl implements Document{
    /**
     * Property change support for all document properties.
     */
    private PropertyChangeSupport support;

    /**
     * The name of this document.
     */
    protected String name;

    /**
     * The immutable root node of this document.
     */
    protected Node rootNode;

    /**
     * A list of all links in this documents.
     */
    protected List<Link> links;

    /**
     * The UndoRedo.Manager for all editors showing this document.
     */
    protected UndoRedo.Manager undoRedoManager;

    /**
     * True, if the document has been modified since the last save, else False.
     */
    protected boolean modified;

    /**
     * Create a new Document instance.
     */
    DocumentImpl() {
        support = new PropertyChangeSupport(this);
        NodeImpl node = new NodeImpl();
        rootNode = node;
        
        node.setName("New Document");
        node.setDocument(this);
        links = new LinkedList<Link>();
        undoRedoManager = new UndoRedo.Manager();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = name;
        this.name = name;
        support.firePropertyChange(PROPERTY_NAME, oldName, name);
    }

    public Node getRootNode() {
        return rootNode;
    }
    
    
    public Link[] getLinks() {
        return links.toArray(new Link[] {});
    }

    public Link getLinks(int index) throws IndexOutOfBoundsException {
        return links.get(index);
    }
    
    public Link addLink() {
        Link link = new LinkImpl();
        links.add(link);
        support.fireIndexedPropertyChange(PROPERTY_LINKS, links.size()-1, null, link);
        return link;
    }

    public void removeLink(Link link) throws NoSuchElementException{
        int index = links.indexOf(link);
        if(index == -1)
            throw new NoSuchElementException(toString() + " contains no link " + link.toString());
        links.remove(link);
        support.fireIndexedPropertyChange(PROPERTY_LINKS, index, link, null);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return name + " : " + links.size() + " links";
    }

    public Manager getUndoRedoManager() {
        return undoRedoManager;
    }

    public AbstractUndoableEdit createAddChildAction(Node parent) {
        return new AddChildAction(this, parent);
    }

    public AbstractUndoableEdit createAppendChildAction(Node parent, Node childToAppend) {
        return new AppendChildAction(this, parent, childToAppend);
    }

    public AbstractUndoableEdit createRemoveChildAction(Node parent, Node childToRemove) {
        return new RemoveChildAction(this, parent, childToRemove);
    }

    public AbstractUndoableEdit createRenameAction(Node parent, String newName) {
        return new RenameAction(this, parent, newName);
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        boolean oldModified = this.modified;
        this.modified = modified;
        support.firePropertyChange(PROPERTY_MODIFIED, oldModified, modified);
    }

    public AbstractUndoableEdit createReorderAction(Node parent, int[] permutation) {
        return new ReorderAction(this, parent, permutation);
    }


}
