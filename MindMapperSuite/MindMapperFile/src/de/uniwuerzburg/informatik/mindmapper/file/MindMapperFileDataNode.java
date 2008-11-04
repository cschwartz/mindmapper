/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.editorapi.DocumentCookie;
import de.uniwuerzburg.informatik.mindmapper.file.MindMapperFileDataObject.SaveSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

public class MindMapperFileDataNode extends DataNode implements PropertyChangeListener{
    private static final String IMAGE_ICON_BASE = "SET/PATH/TO/ICON/HERE";
    public static final String PROPERTY_DOCUMENT_CHANGED = "mindmapperfiledatanode_documentchanged";
    protected Document document;

    protected Lookup documentLookup;

    protected SaveCookie saveCookie;

    MindMapperFileDataNode(MindMapperFileDataObject obj, Lookup lookup) {
        super(obj, new DocumentChildren(obj.lookup.lookup(DocumentCookie.class).getDocument(), lookup));
        this.document = lookup.lookup(DocumentCookie.class).getDocument();
        this.document.addPropertyChangeListener(this);

        saveCookie = obj.getLookup().lookup(SaveSupport.class);

        getCookieSet().add(lookup.lookup(DocumentCookie.class));
        documentLookup = lookup;
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
            setChildren(new DocumentChildren(document, documentLookup));

        }
        if(evt.getPropertyName().equals(Document.PROPERTY_NAME) ||
                evt.getPropertyName().equals(Document.PROPERTY_ALL)) {
            setDisplayName(document.getName());
        }

        if(evt.getPropertyName().equals(Document.PROPERTY_MODIFIED)) {
            if(evt.getNewValue().equals(Boolean.TRUE)) {
                getDataObject().setModified(true);
                getCookieSet().assign(SaveCookie.class, documentLookup.lookup(SaveCookie.class));
            } else {
                getDataObject().setModified(false);
                getCookieSet().assign(SaveCookie.class);
            }
            fireCookieChange();
        }
    }
}
