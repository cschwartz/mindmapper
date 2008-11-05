package de.uniwuerzburg.informatik.mindmapper.spi;

/**
 * An interface providing the xml element and attribute names for the xml
 * representation of the MindMap documents.
 * @author Christian "blair" Schwartz
 */
public interface XmlFileStorage {
    /**
     * The namespace of the xml document.
     */
    public static final String xmlNamespace = "http://xml.netbeans.org/schema/MindMapperSchema";
    
    /**
     * The root elements name.
     */
    public static final String rootElement = "Root";

    /**
     * The element containing all links.
     */
    public static final String linksElement = "Links";

    /**
     * The children of a node.
     */
    public static final String childElement = "Child";

    /**
     * The name for all link elements.
     */
    public static final String linkElement = "Link";

    /**
     * The name attribute of the root node.
     */
    public static final String rootNameAttribute = "name";

    /**
     * The name attribute of the child attribute.
     */
    public static final String childNameAttribute = "name";

    /**
     * The attribute of the unique id of the children.
     */
    public static final String childIdAttribute = "id";

    /**
     * The name attribute of the links.
     */
    public static final String linkNameAttribute = "name";

    /**
     * The source attribute of the links.
     */
    public static final String linkSourceAttribute = "source";

    /**
     * The target attribute of the links.
     */
    public static final String linkTargetAttribute = "target";
}
