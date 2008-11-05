package de.uniwuerzburg.informatik.mindmapper.file;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import de.uniwuerzburg.informatik.mindmapper.file.cookies.DocumentCookie;
import de.uniwuerzburg.informatik.mindmapper.file.MindMapDataObject.SaveSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import org.openide.cookies.SaveCookie;
import org.openide.loaders.DataNode;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 * A NetBeans Node representing the MindMap Document.
 * @author Christian "blair" Schwartz
 */
public class MindMapDocument extends DataNode implements PropertyChangeListener{
    /**
     * The path to the Document Nodes Icon.
     */
    private static final String IMAGE_ICON_BASE = "SET/PATH/TO/ICON/HERE";

    /**
     * The wrapped document.
     */
    protected Document document;

    /**
     * This nodes lookup.
     */
    protected Lookup lookup;

    /**
     * The save cookie for this document, it will be placed in the lookup if and
     * only if the documents modified property is true.
     */
    protected SaveCookie saveCookie;

    /**
     * Create a new MindMapDocument wrapping the document contained in the
     * MindMapDataObject and using the given lookup.
     * @param obj A MindMapDataObject containing a DocumentCookie.
     * @param lookup The lookup to assign to this NetBeans Node.
     */
    MindMapDocument(MindMapDataObject obj, Lookup lookup) {
        super(obj, new DocumentChildren(obj.lookup.lookup(DocumentCookie.class).getDocument(), lookup));
        this.document = lookup.lookup(DocumentCookie.class).getDocument();
        this.document.addPropertyChangeListener(this);

        saveCookie = obj.getLookup().lookup(SaveSupport.class);

        getCookieSet().add(lookup.lookup(DocumentCookie.class));
        this.lookup = lookup;
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

    /**
     * Gets fired if the document changes.
     * @param evt The change event.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(Document.PROPERTY_ROOT)) {
            setChildren(new DocumentChildren(document, lookup));

        }
        if(evt.getPropertyName().equals(Document.PROPERTY_NAME)) {
            setDisplayName(document.getName());
        }

        if(evt.getPropertyName().equals(Document.PROPERTY_MODIFIED)) {
            if(evt.getNewValue().equals(Boolean.TRUE)) {
                getDataObject().setModified(true);
                getCookieSet().assign(SaveCookie.class, lookup.lookup(SaveCookie.class));
            } else {
                getDataObject().setModified(false);
                getCookieSet().assign(SaveCookie.class);
            }
            fireCookieChange();
        }
    }
}
