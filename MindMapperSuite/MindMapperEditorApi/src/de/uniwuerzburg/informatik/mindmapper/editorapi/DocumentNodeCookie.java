/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.editorapi;

import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;

/**
 *
 * @author blair
 */
public interface DocumentNodeCookie extends Cookie{
    public Node getDocumentNode();
    public void open();
}
