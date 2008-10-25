/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file;

import java.util.LinkedList;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

/**
 *
 * @author blair
 */
public class IndexChildren extends Index.ArrayChildren{
    protected de.uniwuerzburg.informatik.mindmapper.api.Node currentNode;
    protected MindMapperFileDataNode dataNode;
    
    public IndexChildren(de.uniwuerzburg.informatik.mindmapper.api.Node currentNode) {
        this.currentNode = currentNode;
    }
    
    public IndexChildren(de.uniwuerzburg.informatik.mindmapper.api.Node currentNode, MindMapperFileDataNode dataNode) {
        this.currentNode = currentNode;
        this.dataNode = dataNode;
    }
    
    public void setDataNode(MindMapperFileDataNode dataNode) {
        this.dataNode = dataNode;
    }
    
    @Override
    protected List<Node> initCollection() {
        List<Node> childNodes = new LinkedList<Node>();
        for(de.uniwuerzburg.informatik.mindmapper.api.Node child : currentNode.getChildren()) {
            MindMapNode newChild;
            if(child.getChildren().length != 0)
                newChild = new MindMapNode(dataNode, new IndexChildren(currentNode, dataNode), child);
            else
                newChild = new MindMapNode(dataNode, Children.LEAF, child);
            childNodes.add(newChild);
        }
        
        return childNodes;
    }
    
}
