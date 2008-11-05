package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 * The Children of the MindMapDocument NetBeans Node.
 * @author Christian "blair" Schwartz
 */
public class DocumentChildren extends Children.Keys<Integer> {
    /**
     * The key for the root node of the document.
     */
    private static int CHILD_ROOT = 0;

    /**
     * The document wrapped in the NetBeans Node which owns this children.
     */
    protected Document document;

    /**
     * The lookup of the owning MindMapDocument.
     */
    protected Lookup lookup;

    /**
     * Create a new DocumentChildren instance wrapping a document with the
     * given lookup.
     * @param document The document wrapped by the owning node.
     * @param lookup The lookup of the owning node.
     */
    public DocumentChildren(Document document, Lookup lookup) {
        super();
        this.document = document;
        this.lookup = lookup;
    }

    @Override
    protected void addNotify() {
        setKeys(new Integer[] {CHILD_ROOT});
    }

    @Override
    protected Node[] createNodes(Integer key) {
        if(key == CHILD_ROOT) {
            MindMapNode node = new MindMapNode(new NodeChildren(document.getRootNode(), lookup), document.getRootNode(), lookup);
            return new Node[] { node };
        } else {
            return new Node[] {};
        }
    }
    }