/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.file;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.SimpleBeanInfo;
import org.openide.loaders.UniFileLoader;

/**
 * Generated
 * @author Christian "blair" Schwartz
 */
public class MindMapDataLoaderBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            return new BeanInfo[]{Introspector.getBeanInfo(UniFileLoader.class)};
        } catch (IntrospectionException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public Image getIcon(int type) {
        return super.getIcon(type); // TODO add a custom icon here: Utilities.loadImage(..., true)

    }
}
