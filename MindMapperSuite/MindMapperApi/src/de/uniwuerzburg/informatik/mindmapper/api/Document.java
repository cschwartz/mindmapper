/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.api;

import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;
import javax.swing.undo.AbstractUndoableEdit;
import org.openide.awt.UndoRedo;

/**
 *
 * @author blair
 */
public interface Document{
    public static final String PROPERTY_NAME = "document_name";
    public static final String PROPERTY_LINKS = "document_links";
    public static final String PROPERTY_ALL = "document_all";
    public static final String PROPERTY_ROOT = "document_root";
    public static final String PROPERTY_MODIFIED = "document_modified";
    
    public String getName();
    public void setName(String name);
    
    public Node getRootNode();
    
    public Link[] getLinks();
    public Link getLinks(int index) throws IndexOutOfBoundsException;
    
    public Link addLink();
    public void removeLink(Link link) throws NoSuchElementException;
    
    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);

    public UndoRedo.Manager getUndoRedoManager();

    public AbstractUndoableEdit createAddChildAction(Node parent);
    public AbstractUndoableEdit createAppendChildAction(Node parent, Node childToAppend);
    public AbstractUndoableEdit createRemoveChildAction(Node parent, Node childToRemove);
    public AbstractUndoableEdit createRenameAction(Node parent, String newName);
    public AbstractUndoableEdit createReorderAction(Node parent, int[] permutation);

    public boolean isModified();
    public void setModified(boolean modified);
}
