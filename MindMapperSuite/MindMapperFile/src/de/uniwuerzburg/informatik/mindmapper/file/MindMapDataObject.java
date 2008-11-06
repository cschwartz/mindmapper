    package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.api.MindMapFactory;
import de.uniwuerzburg.informatik.mindmapper.file.cookies.DocumentCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.MindMapEditorFactory;
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

/**
 * The DataObject for a MindMap Document. Wrapps the XML File containing the
 * MindMap Document.
 * @author Christian "blair" Schwartz.
 */
public class MindMapDataObject extends MultiDataObject implements DocumentCookie{

    /**
     * The wrapped document.
     */
    protected Document document;

    /**
     * This nodes lookup.
     */
    protected Lookup lookup;

    /**
     * The Netbeans Node representing the MindMap Document.
     */
    protected MindMapDocument documentNode;

    /**
     * The save support instance for this data node.
     */
    protected SaveSupport saveSupport;

    /**
     * This nodes document cookie.
     */
    protected DocumentCookieImpl documentNodeCookieImpl;

    /**
     * Create a new MindMapDataObject representing the xml file wrapped in the
     * file object.
     * @param pf The primary file containing the xml file representing the
     * MindMap Document.
     * @param loader The DataLoader for this filetype.
     * @throws org.openide.loaders.DataObjectExistsException If there already
     * exists a a dataobject for the primaryFile.
     * @throws java.io.IOException If an error occurs during loading.
     */
    public MindMapDataObject(FileObject pf, MindMapDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) new OpenSupportImpl(getPrimaryEntry()));
        saveSupport = new SaveSupport();
        getCookieSet().assign(SaveSupport.class, saveSupport);

        documentNodeCookieImpl = new DocumentCookieImpl();

        getCookieSet().add(documentNodeCookieImpl);
    }

    /**
     * Return the wrapped document.
     * @return The wrapped document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Returns an Node representing the file which allows the opening of the
     * editor
     * @return The node representing the file.
     */
    @Override
    protected Node createNodeDelegate() {
        AbstractNode node = new AbstractNode(Children.LEAF, getLookup());
        node.setDefaultAction(OpenAction.get(OpenAction.class));
        node.setName(MindMapDataObject.this.getName());
        return node;
    }

    @Override
    public Lookup getLookup() {
        if(lookup == null) {
            lookup = new ProxyLookup(new Lookup[] {super.getCookieSet().getLookup(), Lookups.fixed(this, new OpenSupportImpl(getPrimaryEntry()))});
        }
        return lookup;
    }

    /**
     * Support for opening and closing the represented mindmap in an editor.
     */
    protected class OpenSupportImpl extends OpenSupport implements OpenCookie, CloseCookie {

        public OpenSupportImpl(MindMapDataObject.Entry entry) {
            super(entry);
        }

        @Override
        protected CloneableTopComponent createCloneableTopComponent() {
            if(document == null) {
                MindMapDataObject.this.lookup.lookup(de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie.class).getDocumentNode();
                getCookieSet().assign(DocumentCookie.class, new DocumentCookie() {

                    public Document getDocument() {
                        return document;
                    }
                });

            }
            MindMapDataObject dataObject = (MindMapDataObject) entry.getDataObject();
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

    /**
     * Support for save and save as for this document.
     */
    public class SaveSupport implements SaveCookie, SaveAsCapable{

        /**
         * Save this document.
         * @throws java.io.IOException If saving fails.
         */
        public void save() throws IOException {
            Lookup.getDefault().lookup(MindMapFactory.class).saveDocument(MindMapDataObject.this.getPrimaryFile(), document);
            document.setModified(false);
        }

        /*+
         * Save this document in folder as a file with the name 'name'.
         * @param folder The folder to save in.
         * @param name The name of the file to save as.
         */
        public void saveAs(FileObject folder, String name) throws IOException {
            FileObject file = folder.createData(name);
            Lookup.getDefault().lookup(MindMapFactory.class).saveDocument(file, document);
        }
        
    }

    /**
     * A Cookie supporting several operations on a document.
     */
    protected class DocumentCookieImpl implements de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie {
        
        /**
         * Get the document node representing the MindMap document.
         * @return The Node representing the MindMap document.
         */
        public org.openide.nodes.Node getDocumentNode() {
            if(documentNode == null) {
                MindMapFactory factory = Lookup.getDefault().lookup(MindMapFactory.class);
                document = factory.loadDocument(getPrimaryEntry().getFile());
                if(document == null)
                    document = factory.createDocument();

                documentNode = new MindMapDocument(MindMapDataObject.this, getLookup());
            }
            return documentNode;
        }

        /**
         * Checks if the document has been modified.
         * @return True, if there have been modifications since the last save,
         * else False.
         */
        public boolean isModified() {
            return document.isModified();
        }

        /**
         * Get the documents UndoRedo.Manager.
         * @return The documents UndoRedo.Manager.
         */
        public Manager getUndoRedoManager() {
            return document.getUndoRedoManager();
        }
    }
}
