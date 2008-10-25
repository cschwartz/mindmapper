/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.editorapi;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import org.openide.nodes.Node.Cookie;

/**
 *
 * @author blair
 */
public interface NodeCookie extends Cookie{
    Node getNode(); 
}
