/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.api;

import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;
import javax.swing.undo.StateEditable;

/**
 *
 * @author blair
 */
public interface Node{
    public static final String PROPERTY_NAME = "node_name";
    public static final String PROPERTY_CHILDREN = "node_children";
    public static final String PROPERTY_ALL = "node_all";
    
    public String getName();
    public void setName(String name);
    
    public Node[] getChildren();
    public Node getChildren(int index) throws IndexOutOfBoundsException;
    
    public void addChild(Node node);
    public void removeChild(Node node) throws NoSuchElementException;
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
    
    public Node copy();
}
