package de.uniwuerzburg.informatik.mindmapper.file.cookies;

import de.uniwuerzburg.informatik.mindmapper.api.Document;
import org.openide.nodes.Node.Cookie;

/**
 * A cookie used to obtain the MindMap document wrapped in a NetBeans Node.
 * @author Christian "blair" Schwartz
 */
public interface DocumentCookie extends Cookie{
    /**
     * Get the wrapped document.
     * @return The wrapped document.
     */
    public Document getDocument();
}
