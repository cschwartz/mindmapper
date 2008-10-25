/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie;
import de.uniwuerzburg.informatik.mindmapper.editorapi.ModifiedCookie;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.Node.Cookie;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

public class MindMapperFileDataNode extends DataNode implements PropertyChangeListener, SaveAsCapable{
    private static final String IMAGE_ICON_BASE = "SET/PATH/TO/ICON/HERE";
    public static final String PROPERTY_DOCUMENT_CHANGED = "mindmapperfiledatanode_documentchanged";
    protected Document document;
    
    protected SaveCookie saveCookie = new SaveCookie() {

        public void save() throws IOException {
            getDataObject().setModified(false);
        }
    };
    
    public MindMapperFileDataNode(MindMapperFileDataObject obj) {
        super(obj, new DocumentChildren(obj.lookup.lookup(Document.class)));
        this.document = obj.lookup.lookup(Document.class);
        this.document.addPropertyChangeListener(this);

        getDataObject().setModified(true);
        getCookieSet().add(addModifiedCookie());
        ((DocumentChildren)getChildren()).setDataNode(this);
//        setIconBaseWithExtension(IMAGE_ICON_BASE);
    }

    MindMapperFileDataNode(MindMapperFileDataObject obj, Lookup lookup) {
        super(obj, new DocumentChildren(obj.lookup.lookup(Document.class)));
        this.document = obj.lookup.lookup(Document.class);
        this.document.addPropertyChangeListener(this);

        getCookieSet().add(saveCookie);
        getDataObject().setModified(true);
        
        getCookieSet().add(addModifiedCookie());
        getCookieSet().add(lookup.lookup(DocumentCookie.class));
        
        ((DocumentChildren)getChildren()).setDataNode(this);

        //        setIconBaseWithExtension(IMAGE_ICON_BASE);
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
                    return document.getName();
                }

                @Override
                public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    document.setName(val);
                }
            });
        }
        return sheet;
    }

//    @Override
//    protected Sheet createSheet() {
//        Sheet s = super.createSheet();
//        Sheet.Set ss = s.get(Sheet.PROPERTIES);
//        if (ss == null) {
//            ss = Sheet.createPropertiesSet();
//            s.put(ss);
//        }
//        // TODO add some relevant properties: ss.put(...)
//        return s;
//    }
    
    public Document getDocument() {
        return document;
    }

    @Override
    public boolean canCut() {
        return true;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Document.PROPERTY_ROOT) ||
                evt.getPropertyName().equals(Document.PROPERTY_ALL)) {
            setChildren(new DocumentChildren(document, this));

        }
        if(evt.getPropertyName().equals(Document.PROPERTY_NAME) ||
                evt.getPropertyName().equals(Document.PROPERTY_ALL)) {
            setDisplayName(document.getName());
        }

        if(evt.getPropertyName().equals(PROPERTY_DOCUMENT_CHANGED)) {
            if(evt.getNewValue().equals(Boolean.TRUE)) {
                getDataObject().setModified(true);
                getCookieSet().assign(SaveCookie.class, saveCookie);
//                getCookieSet().assign(SaveCookie.class, saveCookie);
//                firePropertyChange(PROPERTY_DOCUMENT_CHANGED, null, true);
            } else {
                getDataObject().setModified(false);
                getCookieSet().assign(SaveCookie.class);
//                firePropertyChange(PROPERTY_DOCUMENT_CHANGED, null, true);
//                getCookieSet().assign(SaveCookie.class);
            }
            fireCookieChange();
        }
    }

    private Cookie addModifiedCookie() {
        return new ModifiedCookie() {

            protected PropertyChangeSupport support = new PropertyChangeSupport(this);
            protected boolean isModified = false;

            public void addPropertyChangeListener(PropertyChangeListener listener) {
                support.addPropertyChangeListener(listener);
            }

            public void removePropertyChangeListener(PropertyChangeListener listener) {
                support.removePropertyChangeListener(listener);
            }

            public boolean isModified() {
                return isModified;
            }

            public void setModified(boolean isModified) {
                boolean oldIsModified = this.isModified;
                this.isModified = isModified;
                support.firePropertyChange(PROPERTY_MODIFIED, oldIsModified, isModified);
                
                if(isModified) {
                    getCookieSet().assign(SaveCookie.class, saveCookie);
                }
                else {
                    System.out.println("don't save");
                    getCookieSet().assign(SaveCookie.class);
                }
                fireCookieChange();
            }
        };
    }

    public void saveAs(FileObject folder, String name) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
