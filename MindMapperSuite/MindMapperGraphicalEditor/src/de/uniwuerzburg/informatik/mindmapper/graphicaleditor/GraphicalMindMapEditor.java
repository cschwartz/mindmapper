/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GraphicalMindMapEditor.java
 *
 * Created on 07.11.2008, 10:16:39
 */

package de.uniwuerzburg.informatik.mindmapper.graphicaleditor;

import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.UndoRedo;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.SaveCookie;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author blair
 */
public class GraphicalMindMapEditor extends CloneableTopComponent implements ExplorerManager.Provider, Externalizable{

    protected MultiDataObject dataObject;

    protected Node documentNode;

    protected Node rootNode;

    protected MindMapGraphScene scene;

    protected ExplorerManager explorerManager;

    public GraphicalMindMapEditor() {
        initComponents();

        explorerManager = new ExplorerManager();

        scene = new MindMapGraphScene(explorerManager);
        scenePane.setViewportView(scene.createView());

        ActionMap map = getActionMap();

        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(explorerManager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(explorerManager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(explorerManager));
        map.put("delete", ExplorerUtils.actionDelete(explorerManager, true));

        associateLookup(new ProxyLookup(new Lookup[] {ExplorerUtils.createLookup(explorerManager, getActionMap())}));
    }

    /** Creates new form GraphicalMindMapEditor */
    public GraphicalMindMapEditor(MultiDataObject dataObject) {
        this();
        initFrom(dataObject);
    }

    protected void initFrom(MultiDataObject dataObject) {
        this.dataObject = dataObject;
        documentNode = dataObject.getLookup().lookup(DocumentCookie.class).getDocumentNode();
        rootNode = documentNode.getChildren().getNodes()[0];
        scene.setRootNode(rootNode);
        explorerManager.setRootContext(rootNode);
        setName(documentNode.getName());
    }

    @Override
    public UndoRedo getUndoRedo() {
        return dataObject.getLookup().lookup(DocumentCookie.class).getUndoRedoManager();
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scenePane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(scenePane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scenePane;
    // End of variables declaration//GEN-END:variables

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

        @Override
    public int getPersistenceType() {
        return PERSISTENCE_ONLY_OPENED;
    }

    @Override
    protected String preferredID() {
        return "GraphicalMindMapEditor";
    }

    @Override
    public void writeExternal(ObjectOutput arg0) throws IOException {
        super.writeExternal(arg0);

        arg0.writeObject(dataObject);
    }

    @Override
    public void readExternal(ObjectInput arg0) throws IOException, ClassNotFoundException {
        super.readExternal(arg0);

        dataObject = (MultiDataObject)arg0.readObject();
        initFrom(dataObject);
    }

    /**
     * Check if the editor can be closed or if changes have to be saved.
     * @return True, if the editor should be closed, False if else.
     */
    @Override
    public boolean canClose() {
        boolean isModified = dataObject.getCookie(DocumentCookie.class).isModified();
        Enumeration<CloneableTopComponent> components = getReference().getComponents();
        int numComponents = 0;
        while(components.hasMoreElements()) {
            components.nextElement();
            numComponents++;
        }

        //If more than one cloned version of the editor exists, the editor
        //can be closed.
        //If the document is not modified it can be closed.
        if(numComponents > 1 || !isModified)
            return true;
        else {
            NotifyDescriptor unsavedChangedDialog = new NotifyDescriptor.Confirmation(
                    "Do you want to save before quitting",
                    "Unsaved changes");
            Object returnValue = DialogDisplayer.getDefault().notify(unsavedChangedDialog);

            if(returnValue == NotifyDescriptor.YES_OPTION) {
                //save the document and quit.
                try {
                documentNode.getCookie(SaveCookie.class).save();
                dataObject.getCookie(CloseCookie.class).close();
                return true;
                } catch(IOException e) {
                    NotifyDescriptor n = new NotifyDescriptor.Exception(e);
                    DialogDisplayer.getDefault().notify(n);
                    dataObject.getCookie(CloseCookie.class).close();
                    return false;
                }
            } else if(returnValue.equals(NotifyDescriptor.NO_OPTION)) {
                //don't save the document and quit.
                dataObject.getCookie(CloseCookie.class).close();
                return true;
            } else {
                //cancel the closing.
                return false;
            }

        }
    }

}
