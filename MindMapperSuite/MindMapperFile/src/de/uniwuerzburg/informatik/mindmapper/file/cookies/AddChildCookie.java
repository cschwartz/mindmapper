/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file.cookies;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import org.openide.nodes.Node.Cookie;

/**
 *
 * @author blair
 */
public interface AddChildCookie extends Cookie{
    public void addChild();
    public void addChild(Node child);
}
