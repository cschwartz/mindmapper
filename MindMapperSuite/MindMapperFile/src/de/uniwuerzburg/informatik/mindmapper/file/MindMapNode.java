/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentNodeCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.NodeCookie;
import de.uniwuerzburg.informatik.mindmapper.file.cookies.AddChildCookie;
import de.uniwuerzburg.informatik.mindmapper.file.cookies.RemoveChildCookie;
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
import org.openide.actions.NewAction;
import org.openide.actions.PasteAction;
import org.openide.actions.RenameAction;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author blair
 */
class MindMapNode extends AbstractNode implements PropertyChangeListener{
    static int i = 0;
    
    protected Node node;
    protected Document document;
    protected Lookup lookup;
    
    public MindMapNode(Document document, Children children, Node node, Lookup lookup) {
        super(children);
        this.node = node;
        this.node.addPropertyChangeListener(this);
        this.document = document;
        this.document.addPropertyChangeListener(this);
        this.lookup = lookup;
        
        getCookieSet().add(new AddChildCookie() {

            public void addChild() {
                MindMapNode.this.document.createAddChildAction(MindMapNode.this.node);
            }

            public void addChild(Node child) {
                MindMapNode.this.document.createAppendChildAction(MindMapNode.this.node, child);
            }
        });
        
        getCookieSet().add(new RemoveChildCookie() {

            public void removeAsChild() {
                try {
                    MindMapNode.this.destroy();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        getCookieSet().add(new NodeCookie() {

            public Node getNode() {
                return MindMapNode.this.node;
            }
            
        });

        if(document.isModified())
            getCookieSet().add(lookup.lookup(SaveCookie.class));
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

        if(!(getParentNode() instanceof MindMapperFileDataNode)) {
            actionList.add(SystemAction.get(CutAction.class));
            actionList.add(SystemAction.get(DeleteAction.class));
        }

        actionList.add(SystemAction.get(NewAction.class));
        actionList.add(SystemAction.get(CopyAction.class));
        actionList.add(SystemAction.get(PasteAction.class));
        actionList.add(SystemAction.get(RenameAction.class));

        return actionList.toArray(new Action[0]);
    }

    @Override
    public void destroy() throws IOException {
        Node parent = getParentNode().getLookup().lookup(NodeCookie.class).getNode();
        document.createRemoveChildAction(parent, node);
        
    }
    
    
   
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Node.PROPERTY_CHILDREN) || 
                evt.getPropertyName().equals(Node.PROPERTY_ALL)) {
            setChildren(new NodeChildren(document, node, lookup));
        }
        if(evt.getPropertyName().equals(Node.PROPERTY_NAME) ||
                evt.getPropertyName().equals(Node.PROPERTY_ALL)) {
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
        document.createRenameAction(node, s);
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
                        getLookup().lookup(AddChildCookie.class).addChild(transferedNode.copy());
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
    public Transferable clipboardCut() throws IOException {
        destroy();
        return super.clipboardCut();
    }

    @Override
    public boolean canCut() {
        return !(getParentNode() instanceof MindMapperFileDataNode);
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return !(getParentNode() instanceof MindMapperFileDataNode);
    }

    @Override
    public boolean canRename() {
        return true;
    }

    class NewNodeType extends NewType{

        @Override
        public void create() throws IOException {
            MindMapNode.this.document.createAddChildAction(MindMapNode.this.node);
        }

        @Override
        public String getName() {
            return "Node";
        }
    }
}
