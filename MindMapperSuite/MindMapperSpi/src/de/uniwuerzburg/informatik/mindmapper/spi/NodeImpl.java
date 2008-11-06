package de.uniwuerzburg.informatik.mindmapper.spi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An implementation of the Node interface.
 * @author Christian "blair" Schwartz
 */
public class NodeImpl implements Node{
    /**
     * Property change support for all node properties.
     */
    protected PropertyChangeSupport support;

    /**
     * The nodes name property.
     */
    protected String name;

    /**
     * The nodes children.
     */
    protected List<Node> children;

    /**
     * The document owning this node.
     */
    protected Document document;

    /**
     * Create a new node.
     */
    public NodeImpl() {
        support = new PropertyChangeSupport(this);
        children = new LinkedList<Node>();
        name = "New node";
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
        ((NodeImpl)node).setDocument(document);
        support.fireIndexedPropertyChange(PROPERTY_CHILDREN, children.size()-1, null, node);
    }

    /**
     * Add a node as a child at the given index.
     * @param node The child to add.
     * @param index The index to add the child at.
     */
    public void addChild(NodeImpl node, int index) {
        children.add(index, node);
        node.setDocument(document);
        support.fireIndexedPropertyChange(PROPERTY_CHILDREN, index, null, node);
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
