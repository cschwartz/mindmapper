/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import org.openide.nodes.Children;

public class NodeChildren extends Children.Keys<Node>{

        protected Node node;
        protected Document document;
        
        public NodeChildren(Document document, Node node) {
            this.node = node;
            this.document = document;
        }
        
        @Override
        protected void addNotify() {
            Node keys[] = new Node[node.getChildren().length];
            for(int i = 0; i < node.getChildren().length;i++) {
                keys[i] = node.getChildren(i);
            }
            setKeys(keys);
        }

        @Override
        protected org.openide.nodes.Node[] createNodes(Node key) {
            if(key.getChildren().length != 0) {
                return new org.openide.nodes.Node[] { new MindMapNode(document, new NodeChildren(document, key), key) };
            } else
                return new org.openide.nodes.Node[] { new MindMapNode(document, Children.LEAF, key)};
        }
    }