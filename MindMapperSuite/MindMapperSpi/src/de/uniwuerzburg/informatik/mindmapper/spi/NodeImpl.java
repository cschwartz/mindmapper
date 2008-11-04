/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author blair
 */
public class NodeImpl implements Node{

    private static final String PROPERTY_INDEX = "nodeimpl_index";
    protected PropertyChangeSupport support;
    protected String name;
    protected List<Node> children;
    Document document;

    public NodeImpl() {
        support = new PropertyChangeSupport(this);
        children = new LinkedList<Node>();
        name = "";        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        support.firePropertyChange(PROPERTY_NAME, oldName, name);
    }

    public Node[] getChildren() {
        return children.toArray(new Node[] {});
    }

    public Node getChildren(int index) throws IndexOutOfBoundsException {
        return children.get(index);
    }

    public void addChild(Node node) {
        children.add(node);
        support.fireIndexedPropertyChange(PROPERTY_CHILDREN, children.size()-1, null, node);
    }

    public void removeChild(Node node) throws NoSuchElementException{
        int index = children.indexOf(node);
        if(index == -1)
            throw new NoSuchElementException(node.toString() + " is not a child of " + toString());
        children.remove(node);
        support.fireIndexedPropertyChange(PROPERTY_CHILDREN, index, node, null);
    }

    public void reorder(int[] permutation) {
        Node[] nodes = new Node[permutation.length];
        for(int i = 0; i < permutation.length; i++) {
            nodes[permutation[i]] =  children.get(i);
        }
        children.clear();
        for(Node node : nodes) {
            children.add(node);
        }
        support.firePropertyChange(PROPERTY_CHILDREN, null, children);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return name + " : " + children.size() + " children";
    }

    public Node copy() {
        NodeImpl newNode = new NodeImpl();
        newNode.setName(getName());
        for(Node child : children) {
            newNode.addChild(child.copy());
        }
        
        return newNode;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
