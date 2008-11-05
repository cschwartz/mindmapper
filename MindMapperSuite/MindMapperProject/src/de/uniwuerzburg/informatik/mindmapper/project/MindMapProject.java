package de.uniwuerzburg.informatik.mindmapper.project;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author blair
 */
class MindMapProject implements Project{

    public final static String mindmapDirectory = "mindmaps";
    
    protected FileObject projectDirectory;
    protected ProjectState state;
    protected Lookup lookup;
    protected MindMapLogicalView logicalView = new MindMapLogicalView(this);
    
    public MindMapProject(FileObject projectDirectory, ProjectState state) {
        this.projectDirectory = projectDirectory;
        this.state = state;
    }

    public FileObject getProjectDirectory() {
        return projectDirectory;
    }

    public FileObject getMindMapDirectory(boolean create) {
        FileObject directory = projectDirectory.getFileObject(mindmapDirectory);
        
        if(directory == null && create) {
            try {
                directory = directory.createFolder(mindmapDirectory);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        return directory;
    }
    
    public Lookup getLookup() {
        if(lookup == null) {
            lookup = Lookups.fixed(new Object[] {this,
                state,
                new ActionProviderImpl(),
                new ProjectInformationImpl(),
                loadProperties(),
                logicalView
            });
        }
        return lookup;
    }
    
    protected Properties loadProperties() {
        FileObject propertiesFile = projectDirectory.getFileObject(MindMapProjectFactory.projectDirectory + "/" + MindMapProjectFactory.projectFile);
        Properties properties = new NotifyProperties(state);
        if(propertiesFile != null) {
            try {
                properties.load(propertiesFile.getInputStream());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        
        return properties;
    }

    protected final class ActionProviderImpl implements ActionProvider {

        public String[] getSupportedActions() {
            return new String[0];
        }

        public void invokeAction(String arg0, Lookup arg1) throws IllegalArgumentException {
            //NOP
        }

        public boolean isActionEnabled(String arg0, Lookup arg1) throws IllegalArgumentException {
            return false;
        }
    };
    
    protected final class ProjectInformationImpl implements ProjectInformation {

        public String getName() {
            return projectDirectory.getName();
        }

        public String getDisplayName() {
            return getName();
        }

        public Icon getIcon() {
            return new ImageIcon(Utilities.loadImage("de/uniwuerzburg/informatik/mindmapper/project/lightbulb.png"));
        }

        public Project getProject() {
            return MindMapProject.this;
        }

        public void addPropertyChangeListener(PropertyChangeListener arg0) {
            //NOP
        }

        public void removePropertyChangeListener(PropertyChangeListener arg0) {
            //NOP
        }
    }
     
    protected final class NotifyProperties extends Properties {
        protected final ProjectState state;

        public NotifyProperties(ProjectState state) {
            this.state = state;
        }

        @Override
        public synchronized Object put(Object key, Object value) {
            Object result = super.put(key, value);
            if(((result == null) != (value == null)) ||
                    result != null && 
                    value != null &&
                    !value.equals(result)) {
                state.markModified();
            }
            
            return result;
        }
    }
    
    protected final class MindMapLogicalView implements LogicalViewProvider {

        protected MindMapProject project;
        
        public MindMapLogicalView(MindMapProject project) {
            this.project = project;
        }
        
        public Node createLogicalView() {
            FileObject mindMapsFolder = getMindMapDirectory(true);
            DataFolder mindMaps = DataFolder.findFolder(mindMapsFolder);
            Node mindMapsNode = mindMaps.getNodeDelegate();
            return new MindMapsNode(mindMapsNode, project);
        }

        public Node findPath(Node root, Object target) {
            return null;
        }
        
        private class MindMapsNode extends FilterNode {
            
            protected MindMapProject project;
            
            public MindMapsNode(Node node, MindMapProject project) {
                super(node, new FilterNode.Children(node),
                        new ProxyLookup(new Lookup[] {Lookups.singleton(project), node.getLookup()}));
                
                this.project = project;
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }
            
            
        }
    }
}
