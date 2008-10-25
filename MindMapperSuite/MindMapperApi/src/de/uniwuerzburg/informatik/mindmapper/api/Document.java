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
public interface Document{
    public static final String PROPERTY_NAME = "document_name";
    public static final String PROPERTY_LINKS = "document_links";
    public static final String PROPERTY_ALL = "document_all";
    public static final String PROPERTY_ROOT = "document_root";
    
    public String getName();
    public void setName(String name);
    
    public Node getRootNode();
    
    public Link[] getLinks();
    public Link getLinks(int index) throws IndexOutOfBoundsException;
    
    public Link addLink();
    public void removeLink(Link link) throws NoSuchElementException;
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
