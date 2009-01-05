/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.graphicaleditor;

import org.openide.nodes.Node;

/**
 *
 * @author blair
 */
public class MindMapPin {
    protected Node node;
    protected int type;

    protected final static int TYPE_IN = 0;
    protected final static int TYPE_OUT = 1;

    MindMapPin(Node node, int type) {
        this.node = node;
        this.type = type;
    }

    public static MindMapPin createInPin(Node node) {
        return new MindMapPin(node, TYPE_IN);
    }

    public static MindMapPin createOutPin(Node node) {
        return new MindMapPin(node, TYPE_OUT);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MindMapPin other = (MindMapPin) obj;
        if (this.node != other.node && (this.node == null || !this.node.equals(other.node))) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.node != null ? this.node.hashCode() : 0);
        hash = 67 * hash + this.type;
        return hash;
    }
}
