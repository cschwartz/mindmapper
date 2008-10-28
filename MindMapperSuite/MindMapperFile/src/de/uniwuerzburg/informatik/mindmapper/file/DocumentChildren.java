/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author blair
 */
public class DocumentChildren extends Children.Keys<Integer> {
    private static int CHILD_ROOT = 0;
    private static int CHILD_LINKS = 1;

    protected Document document;
    protected MindMapperFileDataNode dataNode;

    public DocumentChildren(Document document) {
        super();
        this.document = document;
    }

    DocumentChildren(Document document, MindMapperFileDataNode dataNode) {
        super();
        this.document = document;
        this.dataNode = dataNode;
    }

    void setDataNode(MindMapperFileDataNode dataNode) {
        this.dataNode = dataNode;
    }

    @Override
    protected void addNotify() {
        setKeys(new Integer[] {CHILD_ROOT/*, CHILD_LINKS*/});
    }



    @Override
    protected Node[] createNodes(Integer key) {
        if(key == CHILD_ROOT) {
            MindMapNode node = new MindMapNode(document, new NodeChildren(document, document.getRootNode()), document.getRootNode());
            return new Node[] { node };
//            } else if(key == CHILD_LINKS) {
//                return new Node[] { new MindMapLinks(document) };
        } else {
            return new Node[] {};
        }
    }
    }