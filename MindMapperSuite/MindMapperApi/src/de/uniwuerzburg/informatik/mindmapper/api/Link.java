/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.api;

import java.beans.PropertyChangeListener;
import javax.swing.undo.StateEditable;

/**
 *
 * @author blair
 */
public interface Link{
    public static final String PROPERTY_NAME = "link_name";
    public static final String PROPERTY_SOURCE = "link_source";
    public static final String PROPERTY_TARGET = "link_target";
    public static final String PROPERTY_ALL = "link_all";
    
    public String getName();
    public void setName(String name);
    
    public Node getSource();
    public void setSource(Node source);
    
    public Node getTarget();
    public void setTarget(Node target);
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
