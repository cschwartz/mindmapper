/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.editorapi;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import org.openide.nodes.Node.Cookie;

/**
 *
 * @author blair
 */
public interface DocumentCookie extends Cookie{
    public Document getDocument();
}
