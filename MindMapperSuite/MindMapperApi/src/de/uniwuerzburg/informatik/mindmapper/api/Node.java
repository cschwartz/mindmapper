package de.uniwuerzburg.informatik.mindmapper.api;

import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;

/**
 * MindMaps are recursive trees of Node instances.
 * @author blair
 */
public interface Node{
    /**
     * The nodes name property.
     */
    public static final String PROPERTY_NAME = "node_name";

    /**
     * The nodes children property.
     */
    public static final String PROPERTY_CHILDREN = "node_children";

    /**
     * Gets the name of the node.
     * @return The name of the node.
     */
    public String getName();

    /**
     * Sets a new name for the node.
     * @param name The nodes new name.
     */
    public void setName(String name);

    /**
     * Returns an immutable array of the nodes children.
     * @return The nodes children.
     */
    public Node[] getChildren();

    /**
     * Returns the child with the index-position.
     * @param index The index of the requested child.
     * @return The requested child
     * @throws java.lang.IndexOutOfBoundsException If index >= children.length
     * holds.
     */
    public Node getChildren(int index) throws IndexOutOfBoundsException;

    /**
     * Add a child to the node.
     * @param node The new child.
     */
    public void addChild(Node node);

    /**
     * Remove the given child from this node.
     * @param node The child to remove.
     * @throws java.util.NoSuchElementException If the given node isn't a child
     * of this node.
     */
    public void removeChild(Node node) throws NoSuchElementException;

    /**
     * Add a property change listener to this node.
     * @param listener The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes the given property change listener from this node.
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Returns the document owning this node.
     * @return The owner of this document.
     */
    public Document getDocument();

    /**
     * Creates a deep copy of this node.
     * @return The deep copy.
     */
    public Node copy();
}
