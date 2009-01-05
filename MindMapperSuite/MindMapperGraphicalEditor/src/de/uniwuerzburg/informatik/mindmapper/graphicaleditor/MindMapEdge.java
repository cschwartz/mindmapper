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
public class MindMapEdge {
    protected final static int TYPE_TREE = 0;
    protected final static int TYPE_LINK = 1;

    protected Node source;
    protected Node target;

    protected int type;

    private static int nextId;
    private int id;

    MindMapEdge(Node source, Node target, int type) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.id = nextId++;
    }

    public static MindMapEdge createTreeLink(Node source, Node target) {
        return new MindMapEdge(source, target, TYPE_TREE);
    }

    public static MindMapEdge createLink(Node source, Node target) {
        return new MindMapEdge(source, target, TYPE_LINK);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MindMapEdge other = (MindMapEdge) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + this.id;
        return hash;
    }


}
