/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.util.Lookup;

public class NodeChildren extends Children.Keys<Node> implements PropertyChangeListener{

        protected Node node;
        protected Document document;
        protected Lookup lookup;
        
        public NodeChildren(Document document, Node node, Lookup lookup) {
            this.node = node;
            this.document = document;
            this.lookup = lookup;
        }
        
        @Override
        protected void addNotify() {
            node.addPropertyChangeListener(this);
            propertyChange(null);
        }

//        @Override
//        protected void removeNotify() {
//            node.removePropertyChangeListener(this);
//            setKeys(Collections.EMPTY_SET);
//        }
        @Override
        protected org.openide.nodes.Node[] createNodes(Node key) {
            if(key.getChildren().length != 0) {
                return new org.openide.nodes.Node[] { new MindMapNode(document, new NodeChildren(document, key, lookup), key, lookup) };
            } else
                return new org.openide.nodes.Node[] { new MindMapNode(document, key, lookup)};
        }

        public Index getIndex() {
            return new IndexImpl();
        }

    public void propertyChange(PropertyChangeEvent evt) {
        Node keys[] = new Node[node.getChildren().length];
        for(int i = 0; i < node.getChildren().length;i++) {
            keys[i] = node.getChildren(i);
        }
        setKeys(keys);
    }

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