package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.file.cookies.NodeCookie;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.actions.NewAction;
import org.openide.actions.PasteAction;
import org.openide.actions.RenameAction;
import org.openide.actions.ReorderAction;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;

/**
 * A NetBeans Node wrapping a MindMap Node.
 * @author Christian "blair" Schwartz.
 */
class MindMapNode extends AbstractNode implements PropertyChangeListener{
    /**
     * The wrapped node.
     */
    protected Node node;

    /**
     * This nodes lookup.
     */
    protected Lookup lookup;

    /**
     * Create a new NetBeans Node wrapping the MindMap Node.
     * @param children The children of this node.
     * @param node The node to wrap.
     * @param lookup The lookup to use.
     */
    public MindMapNode(Children children, Node node, Lookup lookup) {
        super(children);

        this.node = node;
        this.node.addPropertyChangeListener(this);
        this.node.getDocument().addPropertyChangeListener(this);

        this.lookup = lookup;

        getCookieSet().add(new NodeCookie() {

            public Node getNode() {
                return MindMapNode.this.node;
            }

        });

        getCookieSet().add(new IndexImpl());

        if(node.getDocument().isModified())
                getCookieSet().add(lookup.lookup(SaveCookie.class));
    }

    /**
     * Add a child to the wrapped MindMap Node.
     * @param child
     */
    private void addChild(Node child) {
        node.getDocument().createAppendChildAction(MindMapNode.this.node, child);
    }

    @Override
    public NewType[] getNewTypes() {
        return new NewType[] { new NewNodeType() };
    }

    @Override
    public String getDisplayName() {
        return node.getName();
    }
    
    @Override
    public Action[] getActions(boolean context) {
        List<Action> actionList = new LinkedList<Action>();
        for(Action action : super.getActions(context)) {
            actionList.add(action); 
        }
        
        Lookup lkp = Lookups.forPath("ContextActions/MindMapNode");
        actionList.addAll(lkp.lookupAll(Action.class));

        if(!(getParentNode() instanceof MindMapDocument)) {
            actionList.add(SystemAction.get(CutAction.class));
            actionList.add(SystemAction.get(DeleteAction.class));
        }

        actionList.add(SystemAction.get(NewAction.class));
        actionList.add(SystemAction.get(CopyAction.class));
        actionList.add(SystemAction.get(PasteAction.class));
        actionList.add(SystemAction.get(RenameAction.class));
        actionList.add(SystemAction.get(ReorderAction.class));
        actionList.add(SystemAction.get(MoveUpAction.class));
        actionList.add(SystemAction.get(MoveDownAction.class));

        return actionList.toArray(new Action[0]);
    }

    @Override
    public void destroy() throws IOException {
        Node parent = getParentNode().getLookup().lookup(NodeCookie.class).getNode();
        if(parent != null)
            node.getDocument().createRemoveChildAction(parent, node);
    }
    
    
    /**
     * Listen to changes in the document and the wrapped node.
     * @param evt The event to react on.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Node.PROPERTY_CHILDREN)) {
            if(getChildren() == Children.LEAF) {
                setChildren(new NodeChildren(node, lookup));
            }
            else if(node.getChildren().length == 0) {
                setChildren(Children.LEAF);
            }
        }
        if(evt.getPropertyName().equals(Node.PROPERTY_NAME)) {
            setName(node.getName());
            setDisplayName(node.getName());
        }
        if(evt.getPropertyName().equals(Document.PROPERTY_MODIFIED)) {
            if(evt.getNewValue().equals(Boolean.TRUE)) {
                getCookieSet().assign(SaveCookie.class, lookup.lookup(SaveCookie.class));
            }
            else if(evt.getNewValue().equals(Boolean.FALSE)){
                getCookieSet().assign(SaveCookie.class);
            }
        }
    }
    
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set sheetSet = sheet.get(Sheet.PROPERTIES);
        if (sheetSet == null) {
            sheetSet = Sheet.createPropertiesSet();
            sheet.put(sheetSet);
            sheetSet.put(new PropertySupport.ReadWrite<String>(de.uniwuerzburg.informatik.mindmapper.api.Document.PROPERTY_NAME, String.class, "Name", "The Documents name") {

                @Override
                public String getValue() throws IllegalAccessException, InvocationTargetException {
                    return node.getName();
                }

                @Override
                public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    setName(val);
                }
            });
        }
        return sheet;
    }

    @Override
    public void setName(String s) {
        super.setName(s);
        node.getDocument().createRenameAction(node, s);
    }

    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public PasteType getDropType(Transferable t, int action, int index) {
        final org.openide.nodes.Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        if(dropNode != null) {
            final Node transferedNode = dropNode.getLookup().lookup(NodeCookie.class).getNode();
            if(transferedNode != null) {
                return new PasteType() {
                    @Override
                    public Transferable paste() throws IOException {
                        addChild(transferedNode.copy());
                        return null;
                    }
                };
            }
        }
        
        return null;
    }

    @Override
    protected void createPasteTypes(Transferable t, List<PasteType> s) {
        super.createPasteTypes(t, s);
        PasteType type = getDropType(t, DnDConstants.ACTION_COPY, -1);
        if(type != null) {
            s.add(type);
        }
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        Node nodeCopy = node.copy();
        Children children;
        if(node.getChildren().length == 0)
            children = Children.LEAF;
        else
            children = new NodeChildren(nodeCopy, lookup);
        MindMapNode copyNode = new MindMapNode(children, nodeCopy, lookup);
        return NodeTransfer.transferable(copyNode, DnDConstants.ACTION_COPY);
    }

    @Override
    public Transferable clipboardCut() throws IOException {
        destroy();
        return clipboardCopy();
    }

    @Override
    public boolean canCut() {
        return !(getParentNode() instanceof MindMapDocument);
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return !(getParentNode() instanceof MindMapDocument);
    }

    @Override
    public boolean canRename() {
        return true;
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
        hash = 53 * hash + (this.node != null ? this.node.hashCode() : 0);
        return hash;
    }

    /**
     * Support creation of child nodes of the same type.
     */
    class NewNodeType extends NewType{

        @Override
        public void create() throws IOException {
            node.getDocument().createAddChildAction(MindMapNode.this.node);
        }

        @Override
        public String getName() {
            return "Node";
        }
    }

    /**
     * An implementation of the index class to support reordering of the
     * children of a MindMap node.
     */
    protected class IndexImpl extends Index.Support {

        @Override
        public org.openide.nodes.Node[] getNodes() {
            return getChildren().getNodes();
        }

        @Override
        public int getNodesCount() {
            return getChildren().getNodesCount();
        }

        @Override
        public void reorder(int[] perm) {
            node.getDocument().createReorderAction(node, perm);
        }

    };
}
