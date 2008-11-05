package de.uniwuerzburg.informatik.mindmapper.file.cookies;

import de.uniwuerzburg.informatik.mindmapper.api.Node;
import org.openide.nodes.Node.Cookie;

/**
 * A cookie used to get a wrapped MindMap Node from a NetBeans Node.
 * @author Christian "blair" Schwartz
 */
public interface NodeCookie extends Cookie{
    /**
     * Get the wrapped MindMap Node.
     * @return The wrapped MindMap Node.
     */
    Node getNode(); 
}
