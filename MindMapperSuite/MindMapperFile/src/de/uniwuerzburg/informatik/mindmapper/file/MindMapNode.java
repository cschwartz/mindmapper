/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.file.actions.AddChildAction;
import de.uniwuerzburg.informatik.mindmapper.file.actions.RemoveChildAction;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import de.uniwuerzburg.informatik.mindmapper.editorapi.NodeCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.UndoRedoManagerCookie;
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
import org.openide.awt.UndoRedo;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.SaveAsCapable;
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
class MindMapNode extends AbstractNode implements PropertyChangeListener, SaveAsCapable{
    static int i = 0;
    
    protected Node node;
    protected MindMapperFileDataNode dataNode;
    
    public MindMapNode(MindMapperFileDataNode dataNode, Children children, Node node) {
        super(children);
        this.node = node;
        this.node.addPropertyChangeListener(this);
        this.dataNode = dataNode;
        this.dataNode.addPropertyChangeListener(this);
        this.dataNode.getDataObject().addPropertyChangeListener(this);
        
        addPropertyChangeListener(dataNode);

        getCookieSet().add(new AddChildCookie() {

            public void addChild() {
                UndoRedo.Manager undoRedo = MindMapNode.this.dataNode.getLookup().lookup(UndoRedoManagerCookie.class).getUndoRedoManager();
                AddChildAction action = new AddChildAction(MindMapNode.this.dataNode.getDataObject(), MindMapNode.this.node);
                undoRedo.addEdit(action);
                markAsModified();
            }

            public void addChild(Node child) {
                UndoRedo.Manager undoRedo = MindMapNode.this.dataNode.getLookup().lookup(UndoRedoManagerCookie.class).getUndoRedoManager();
                AddChildAction action = new AddChildAction(MindMapNode.this.dataNode.getDataObject(), MindMapNode.this.node, child);
                undoRedo.addEdit(action);
                markAsModified();
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
        UndoRedo.Manager undoRedo = MindMapNode.this.dataNode.getLookup().lookup(UndoRedoManagerCookie.class).getUndoRedoManager();
        RemoveChildAction action = new RemoveChildAction(parent, node);
        undoRedo.addEdit(action);
        markAsModified();
    }
    
    
   
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Node.PROPERTY_CHILDREN) || 
                evt.getPropertyName().equals(Node.PROPERTY_ALL)) {
            setChildren(new NodeChildren(dataNode, node));
        }
        if(evt.getPropertyName().equals(Node.PROPERTY_NAME) ||
                evt.getPropertyName().equals(Node.PROPERTY_ALL)) {
            setDisplayName(node.getName());
        }
        if(evt.getPropertyName().equals(DataObject.PROP_MODIFIED)) {
            if(evt.getNewValue().equals(Boolean.TRUE)) {
                getCookieSet().assign(SaveCookie.class, dataNode.getLookup().lookup(SaveCookie.class));
            }
            else {
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
        UndoRedo.Manager undoRedo = MindMapNode.this.dataNode.getLookup().lookup(UndoRedoManagerCookie.class).getUndoRedoManager();
        undoRedo.addEdit(new de.uniwuerzburg.informatik.mindmapper.file.actions.RenameAction(MindMapNode.this.node, s));
        markAsModified();
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
        return true;
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public boolean canRename() {
        return true;
    }

    protected void markAsModified() {
        dataNode.getDataObject().setModified(true);
    }

    public void saveAs(FileObject folder, String name) throws IOException {
        dataNode.saveAs(folder, name);
    }

    class NewNodeType extends NewType{

        @Override
        public void create() throws IOException {
            UndoRedo.Manager undoRedo = MindMapNode.this.dataNode.getLookup().lookup(UndoRedoManagerCookie.class).getUndoRedoManager();
            AddChildAction action = new AddChildAction(MindMapNode.this.dataNode.getDataObject(), MindMapNode.this.node);
            undoRedo.addEdit(action);
            markAsModified();
        }

        @Override
        public String getName() {
            return "Node";
        }
    }
}
