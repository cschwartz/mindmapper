package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.util.Lookup;

/**
 * The children of a wrapped MindMap Node.
 * @author Christian "blair" Schwartz
 */
public class NodeChildren extends Children.Keys<Node> implements PropertyChangeListener{

    /**
     * The wrapped node of the NetBeans node.
     */
    protected Node node;

    /**
     * The lookup of the node.
     */
    protected Lookup lookup;

    /**
     * Create new NetBeans Node Children representing the children of the
     * given node.
     * @param node The MindMap node whose children this instance represents.
     * @param lookup The lookup to use.
     */
    public NodeChildren(Node node, Lookup lookup) {
        this.node = node;
        this.lookup = lookup;
    }

    @Override
    protected void addNotify() {
        node.addPropertyChangeListener(this);
        propertyChange(null);
    }

    @Override
    protected void removeNotify() {
        node.removePropertyChangeListener(this);
        setKeys(Collections.EMPTY_SET);
    }

    @Override
    protected org.openide.nodes.Node[] createNodes(Node key) {
        if(key.getChildren().length == 0) {
            return new org.openide.nodes.Node[] { new MindMapNode(Children.LEAF, key, lookup)};
        } else
            return new org.openide.nodes.Node[] { new MindMapNode(new NodeChildren(key, lookup), key, lookup)};
    }

    /**
     * Listen to children change events of the wrapped node.
     * @param evt The event to react on.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        Node keys[] = new Node[node.getChildren().length];
        for(int i = 0; i < node.getChildren().length;i++) {
            keys[i] = node.getChildren(i);
        }
        setKeys(keys);
    }
}