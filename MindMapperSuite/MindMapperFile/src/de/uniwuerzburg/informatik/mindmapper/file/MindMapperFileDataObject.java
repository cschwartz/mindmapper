/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.MindMapFactory;
import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.MindMapEditorFactory;
import de.uniwuerzburg.informatik.mindmapper.editorapi.UndoRedoManagerCookie;
import java.io.IOException;
import org.openide.awt.UndoRedo;
import org.openide.awt.UndoRedo.Manager;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.CloneableTopComponent;

public class MindMapperFileDataObject extends MultiDataObject implements DocumentCookie, UndoRedoManagerCookie{

    protected Document document;
    protected Lookup lookup;
    protected UndoRedo.Manager undoRedoManager = new UndoRedo.Manager();

    public MindMapperFileDataObject(FileObject pf, MindMapperFileDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) new MindMapOpenSupport(getPrimaryEntry()));
        MindMapFactory factory = Lookup.getDefault().lookup(MindMapFactory.class);
        document = factory.loadDocument(pf);
        if(document == null)
            document = factory.createDocument();
    }

    public Document getDocument() {
        return document;
    }

    @Override
    protected Node createNodeDelegate() {
        return new MindMapperFileDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        if(lookup == null) {
            lookup = new ProxyLookup(new Lookup[] {super.getCookieSet().getLookup(), Lookups.fixed(this, new MindMapOpenSupport(getPrimaryEntry()), document, undoRedoManager)});
//            lookup = Lookups.fixed(new Object[] {document});
//            lookup = super.getCookieSet().getLookup();
        }
        return lookup;
    }

    public Manager getUndoRedoManager() {
        return undoRedoManager;
    }
    
    protected static class MindMapOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

        public MindMapOpenSupport(MindMapperFileDataObject.Entry entry) {
            super(entry);
        }
        
        @Override
        protected CloneableTopComponent createCloneableTopComponent() {
            MindMapperFileDataObject dataObject = (MindMapperFileDataObject) entry.getDataObject();
            CloneableTopComponent editor = Lookup.getDefault().lookup(MindMapEditorFactory.class).createMindMapEditor(dataObject);
            editor.setDisplayName(dataObject.getLookup().lookup(Document.class).getName());
            return editor;
        }

        
    }
}
