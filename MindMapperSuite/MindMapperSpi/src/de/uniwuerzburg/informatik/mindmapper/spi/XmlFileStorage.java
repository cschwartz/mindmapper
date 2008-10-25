/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.spi;

/**
 *
 * @author blair
 */
public interface XmlFileStorage {
    public static final String xmlNamespace = "http://xml.netbeans.org/schema/MindMapperSchema";
    public static final String rootElement = "Root";
    public static final String linksElement = "Links";
    public static final String childElement = "Child";
    public static final String linkElement = "Link";
    public static final String rootNameAttribute = "name";
    public static final String childNameAttribute = "name";
    public static final String childIdAttribute = "id";
    public static final String linkNameAttribute = "name";
    public static final String linkSourceAttribute = "source";
    public static final String linkTargetAttribute = "target";
}
