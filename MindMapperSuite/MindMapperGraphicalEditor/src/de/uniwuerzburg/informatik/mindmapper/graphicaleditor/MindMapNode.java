/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.graphicaleditor;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.awt.Actions;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;

/**
 *
 * @author blair
 */
public class MindMapNode implements NodeListener{
    protected Node node;
    protected Widget widget;
    protected MindMapGraphScene scene;
    protected MindMapEdge edgeToParent;
    protected MindMapNode parent;
    
    protected List<MindMapNode> children;

    public MindMapNode(MindMapGraphScene scene, Node node) {
        this.node = node;
        this.scene = scene;
        this.children = new LinkedList<MindMapNode>();
        node.addNodeListener(this);
        this.widget = scene.addNode(this);
    }

    public String getName() {
        return node.getName();
    }

    public Node getNode() {
        return node;
    }

    public void connect(MindMapNode parent) {
        this.parent = parent;
        edgeToParent = MindMapEdge.createTreeLink(parent.getNode(), getNode());
        scene.addEdge(edgeToParent);
        scene.setEdgeSource(edgeToParent, parent);
        scene.setEdgeTarget(edgeToParent, this);
    }

    public void childrenAdded(NodeMemberEvent ev) {
        updateChildren();
        scene.layout();
    }

    public void updateChildren() {
        for(Node child : node.getChildren().getNodes()) {
            if(!isChild(child)) {
                MindMapNode childNode = new MindMapNode(scene, child);
                childNode.connect(this);
                children.add(childNode);
                if(!child.isLeaf()) {
                    childNode.updateChildren();
                }
                
            }
        }
    }

    protected boolean isChild(Node child) {
        for(MindMapNode childNode : children) {
            if(childNode.getNode().equals(child)) {
                return true;
            }
        }
        return false;
    }

    public void childrenRemoved(NodeMemberEvent ev) {
        
    }

    public void childrenReordered(NodeReorderEvent ev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void nodeDestroyed(NodeEvent ev) {
        removeNode();
        scene.layout();
    }

    protected void removeNode() {
        Widget parentWidget = widget.getParentWidget();
        List<MindMapNode> copyChild = new LinkedList<MindMapNode>(children);

        for(MindMapNode child : copyChild) {
            child.removeNode();
        }
        scene.removeNodeWithEdges(this);
        parent.children.remove(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Node.PROP_NAME)) {
            ((LabelWidget)widget).setLabel((String)evt.getNewValue());
        }
    }

    WidgetAction getPopupAction() {
        PopupMenuProvider provider = new PopupMenuProvider() {

            public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
                JPopupMenu menu = new JPopupMenu();
                for(Action action : node.getActions(true)) {
                    JMenuItem item = new JMenuItem();
                    Actions.connect(item, action, true);
                    menu.add(item);
                }
                return menu;
            }

        };

        return ActionFactory.createPopupMenuAction(provider);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MindMapNode other = (MindMapNode) obj;
        if (this.node != other.node && (this.node == null || !this.node.equals(other.node))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.node != null ? this.node.hashCode() : 0);
        return hash;
    }
}
