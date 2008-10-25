/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.editorapi;

import java.beans.PropertyChangeListener;
import org.openide.nodes.Node.Cookie;

/**
 *
 * @author blair
 */
public interface ModifiedCookie extends Cookie{
    public static final String PROPERTY_MODIFIED = "modifiedcookie_modified";

    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);

    public boolean isModified();
    public void setModified(boolean isModified);
}
