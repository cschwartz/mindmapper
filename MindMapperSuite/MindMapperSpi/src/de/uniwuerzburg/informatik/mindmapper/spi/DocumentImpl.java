/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Link;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.undo.StateEditable;

/**
 *
 * @author blair
 */
public class DocumentImpl implements Document{
    private PropertyChangeSupport support;
    protected String name;
    protected Node rootNode;
    protected List<Link> links;
    
    DocumentImpl() {
        support = new PropertyChangeSupport(this);
        rootNode = new NodeImpl();
        rootNode.setName("New Document");
        links = new LinkedList<Link>();
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
}
