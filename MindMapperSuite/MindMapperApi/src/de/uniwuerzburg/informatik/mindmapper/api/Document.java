package de.uniwuerzburg.informatik.mindmapper.api;

import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;
import javax.swing.undo.AbstractUndoableEdit;
import org.openide.awt.UndoRedo;

/**
 * A Mind Map document consists of a tree like structure of nodes. Any two nodes
 * can be connected by a link.
 * A MindMap should be manipulated by the actions created by its document
 * instance.
 * Each Document has an UndoRedo.Manager which is used to execute all actions
 * created by the document instance.
 * @author Christian "blair" Schwartz
 */
public interface Document{
    /**
     * The document-name property.
     */
    public static final String PROPERTY_NAME = "document_name";

    /**
     * The document-links property.
     */
    public static final String PROPERTY_LINKS = "document_links";

    /**
     * The root node property of the document.
     */
    public static final String PROPERTY_ROOT = "document_root";

    /**
     * The modified property of the document.
     */
    public static final String PROPERTY_MODIFIED = "document_modified";

    /**
     * Returns the documents name.
     * @return The documents name.
     */
    public String getName();

    /**
     * Sets the documents name.
     * @param name The documents name.
     */
    public void setName(String name);

    /**
     * Returns the root node of the MindMap tree.
     * This node can not be removed and it has no peers.
     * @return The root node of this document.
     */
    public Node getRootNode();

    /**
     * Returns a immutable list of all links in the document.
     * @return A list of all links.
     */
    public Link[] getLinks();

    /**
     * Returns the link at the position 'index' of the document.
     * @param index The index of the requested link.
     * @return The link at the position 'index'.
     * @throws java.lang.IndexOutOfBoundsException If index < links.length does not hold.
     */
    public Link getLinks(int index) throws IndexOutOfBoundsException;

    /**
     * Add and return a new link.
     * @return The new link.
     */
    public Link addLink();

    /**
     * Removes the link from the document if it exists.
     * @param link The link to remove.
     * @throws java.util.NoSuchElementException If the link does not exist in the document.
     */
    public void removeLink(Link link) throws NoSuchElementException;

    /**
     * Add a property change listener to the document.
     * @param listener The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a property change listener if it exists.
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Returns the UndoRedo.Manager of this document.
     * @return The UndoRedo.Manager.
     */
    public UndoRedo.Manager getUndoRedoManager();

    /**
     * Create and execute an action to add a new child to the parent node. Adds
     * it to the UndoRedo.Manager.
     * @param parent The parent to add the child too.
     * @return The created and already executed action.
     */
    public AbstractUndoableEdit createAddChildAction(Node parent);

    /**
     * Create and execute an action to append a given child to the the parent
     * node. Appends the action to the UndoRedo.Manager.
     * @param parent The parent to append the child to.
     * @param childToAppend The child to append to the parent.
     * @return The created and already executed action.
     */
    public AbstractUndoableEdit createAppendChildAction(Node parent, Node childToAppend);

    /**
     * Create and execute an action to remove a child from a parent. Append
     * the action to the UndoRedo.Manager.
     * @param parent The parent to remove the child from.
     * @param childToRemove The child to remove from the parent.
     * @return The created and already executed action.
     */
    public AbstractUndoableEdit createRemoveChildAction(Node parent, Node childToRemove);

    /**
     * Create and execute an action to rename a node. Append the action to the
     * UndoRedo.Manager.
     * @param parent The node to rename.
     * @param newName The new name of the node.
     * @return The created and already executed action.
     */
    public AbstractUndoableEdit createRenameAction(Node parent, String newName);

    /**
     * Create and execute an action to reorder the children of the node parent
     * according to a given permutation.
     * @param parent The parent of which to change the order of the children.
     * @param permutation The new order of the children.
     * @return The created and already executed action.
     */
    public AbstractUndoableEdit createReorderAction(Node parent, int[] permutation);

    /**
     * Returns true if the document has been modified since the last save.
     * @return True if it is modified, else false.
     */
    public boolean isModified();

    /**
     * Set the modified status of the document.
     * @param modified True if the document is modified, else false.
     */
    public void setModified(boolean modified);
}
