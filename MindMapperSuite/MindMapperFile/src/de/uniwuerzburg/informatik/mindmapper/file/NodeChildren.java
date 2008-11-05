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
        return new org.openide.nodes.Node[] { new MindMapNode(new NodeChildren(key, lookup), key, lookup)};
    }

    /**
     * Provide a Index for reordering.
     * @return The index used for reordering.
     */
    public Index getIndex() {
        return new IndexImpl();
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

    /**
     * An implementation of the index class to support reordering of the
     * children of a MindMap node.
     */
    protected class IndexImpl extends Index.Support {

        @Override
        public org.openide.nodes.Node[] getNodes() {
            return NodeChildren.this.getNodes();
        }

        @Override
        public int getNodesCount() {
            return NodeChildren.this.getNodesCount();
        }

        @Override
        public void reorder(int[] perm) {
            node.getDocument().createReorderAction(node, perm);
        }

    };
}