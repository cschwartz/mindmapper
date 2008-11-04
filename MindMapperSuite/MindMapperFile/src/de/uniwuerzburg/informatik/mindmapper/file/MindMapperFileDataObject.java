/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.MindMapFactory;
import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentNodeCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.MindMapEditorFactory;
import de.uniwuerzburg.informatik.mindmapper.editorapi.UndoRedoManagerCookie;
import java.io.IOException;
import org.openide.actions.OpenAction;
import org.openide.awt.UndoRedo.Manager;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.CloneableTopComponent;

public class MindMapperFileDataObject extends MultiDataObject implements DocumentCookie, UndoRedoManagerCookie{

    protected Document document;
    protected Lookup lookup;
    protected MindMapperFileDataNode nodeDelegate;

    protected SaveSupport saveSupport;

    protected DocumentNodeCookieImpl documentNodeCookieImpl;

    protected boolean isClosed;

    public MindMapperFileDataObject(FileObject pf, MindMapperFileDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        isClosed = true;
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) new MindMapOpenSupport(getPrimaryEntry()));
        saveSupport = new SaveSupport();
        getCookieSet().assign(SaveSupport.class, saveSupport);

        documentNodeCookieImpl = new DocumentNodeCookieImpl();

        getCookieSet().add(documentNodeCookieImpl);
    }

    public Document getDocument() {
        return document;
    }

    @Override
    protected Node createNodeDelegate() {
        AbstractNode node = new AbstractNode(Children.LEAF, getLookup());
        node.setDefaultAction(OpenAction.get(OpenAction.class));
        node.setName(MindMapperFileDataObject.this.getName());
        return node;
    }

    @Override
    public Lookup getLookup() {
        if(lookup == null) {
            lookup = new ProxyLookup(new Lookup[] {super.getCookieSet().getLookup(), Lookups.fixed(this, new MindMapOpenSupport(getPrimaryEntry()))});
        }
        return lookup;
    }

    public Manager getUndoRedoManager() {
        return document.getUndoRedoManager();
    }

    public void open() {
        MindMapFactory factory = Lookup.getDefault().lookup(MindMapFactory.class);
        document = factory.loadDocument(getPrimaryEntry().getFile());
        if(document == null)
            document = factory.createDocument();

        nodeDelegate = new MindMapperFileDataNode(MindMapperFileDataObject.this, getLookup());
    }

    
    protected class MindMapOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

        public MindMapOpenSupport(MindMapperFileDataObject.Entry entry) {
            super(entry);
        }

        @Override
        protected CloneableTopComponent createCloneableTopComponent() {
            if(document == null) {
                MindMapperFileDataObject.this.open();
                getCookieSet().assign(DocumentCookie.class, new DocumentCookie() {

                    public Document getDocument() {
                        return document;
                    }
                });

            }
            MindMapperFileDataObject dataObject = (MindMapperFileDataObject) entry.getDataObject();
            CloneableTopComponent editor = Lookup.getDefault().lookup(MindMapEditorFactory.class).createMindMapEditor(dataObject);
            return editor;
        }

        @Override
        public boolean close() {
            document = null;
            getCookieSet().assign(DocumentCookie.class);
            setModified(false);
            return true;
        }


    }

    public class SaveSupport implements SaveCookie, SaveAsCapable{

        public void save() throws IOException {
//          Lookup.getDefault().lookup(MindMapFactory.class).saveDocument(MindMapperFileDataObject.this.getPrimaryFile(), document);
            System.out.println("save");
            document.setModified(false);
        }

        public void saveAs(FileObject folder, String name) throws IOException {
            System.out.println("save as");
        }
        
    }

    protected class DocumentNodeCookieImpl implements DocumentNodeCookie {

            public org.openide.nodes.Node getDocumentNode() {
                return nodeDelegate;
            }

            public void open() {
                MindMapperFileDataObject.this.open();
            }

        }
}
