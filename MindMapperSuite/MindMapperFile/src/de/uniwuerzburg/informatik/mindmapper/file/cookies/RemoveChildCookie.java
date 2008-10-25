/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.file.cookies;

import org.openide.nodes.Node.Cookie;

/**
 *
 * @author blair
 */
public interface RemoveChildCookie extends Cookie{
    void removeAsChild();
}
