package de.uniwuerzburg.informatik.mindmapper.api;

import java.beans.PropertyChangeListener;

/**
 * A link connects two nodes of a document.
 * Links are created, stored and destroyed by their documents.
 * @author Christian "blair" Schwartz
 */
public interface Link{
    /**
     * The name-property of the link.
     */
    public static final String PROPERTY_NAME = "link_name";

    /**
     * The source-property of the link.
     */
    public static final String PROPERTY_SOURCE = "link_source";

    /**
     * The target-property of the link.
     */
    public static final String PROPERTY_TARGET = "link_target";

    /**
     * Returns the name of the link.
     * @return The name of the link.
     */
    public String getName();

    /**
     * Sets the name of the link.
     * @param name The new name of the link.
     */
    public void setName(String name);

    /**
     * Returns the source node of the link.
     * @return The source node of the link.
     */
    public Node getSource();

    /**
     * Sets the source node of the link.
     * @param source The source node of the link.
     */
    public void setSource(Node source);

    /**
     * Returns the target node of the link.
     * @return The target node of the link.
     */
    public Node getTarget();

    /**
     * Sets the target node of the link.
     * @param target The new target node of the link.
     */
    public void setTarget(Node target);

    /**
     * Add a property change listener to the link.
     * @param listener The property change listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes the property change listener from the link.
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
