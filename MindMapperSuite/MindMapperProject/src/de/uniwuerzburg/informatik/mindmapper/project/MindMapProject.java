package de.uniwuerzburg.informatik.mindmapper.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
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
 * The MindMap Project class, based on the NetBeans Platform PovRay Tutorial
 * @author Christian "blair" Schwartz
 */
class MindMapProject implements Project{
    /**
     * The name of the subdir which will contain all mindmaps.
     */
    public final static String mindmapDirectory = "mindmaps";

    /**
     * The fileobject for the project directory.
     */
    protected FileObject projectDirectory;

    /**
     * The projects current state.
     */
    protected ProjectState state;

    /**
     * The projects lookup.
     */
    protected Lookup lookup;

    /**
     * The logical view to use.
     */
    protected MindMapLogicalView logicalView = new MindMapLogicalView(this);

    /**
     * Create a new project for the given directory with the given state.
     * @param projectDirectory The project directory.
     * @param state The state of the directory.
     */
    public MindMapProject(FileObject projectDirectory, ProjectState state) {
        this.projectDirectory = projectDirectory;
        this.state = state;
    }

    /**
     * The FileObject for the project directory.
     * @return The FileObject for the project directory.
     */
    public FileObject getProjectDirectory() {
        return projectDirectory;
    }

    /**
     * Return the directory to save the mindmap in.
     * @param create Whether to create the directory if it doesn't exist.
     * @return The FileObject for the mindmap directory.
     */
    public FileObject getMindMapDirectory(boolean create) {
        FileObject directory = projectDirectory.getFileObject(mindmapDirectory);
        
        if(directory == null && create) {
            try {
                directory = directory.createFolder(mindmapDirectory);
            } catch (IOException ex) {
                NotifyDescriptor n = new NotifyDescriptor.Exception(ex);
                DialogDisplayer.getDefault().notify(n);
            }
        }
        
        return directory;
    }

    /**
     * Get this projects lookup.
     * @return The projects lookup.
     */
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

    /**
     * Load the projects properties.
     * @return The projects properties.
     */
    protected Properties loadProperties() {
        FileObject propertiesFile = projectDirectory.getFileObject(MindMapProjectFactory.projectDirectory + "/" + MindMapProjectFactory.projectFile);
        Properties properties = new NotifyProperties(state);
        if(propertiesFile != null) {
            try {
                properties.load(propertiesFile.getInputStream());
            } catch (IOException e){
                NotifyDescriptor n = new NotifyDescriptor.Exception(e);
                DialogDisplayer.getDefault().notify(n);
            }
        }
        
        return properties;
    }

    /**
     * Provide the actions for this project. (None at the moment)
     */
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

    /**
     * Provide information about the project.
     */
    protected final class ProjectInformationImpl implements ProjectInformation {

        /**
         * Property Change Support for the project informations.
         */
        PropertyChangeSupport support = new PropertyChangeSupport(this);

        /**
         * The projects name is the name of its hosting directory.
         * @return The projects name.
         */
        public String getName() {
            return projectDirectory.getName();
        }

        /**
         * The display name is the projects name.
         * @return The display name.
         */
        public String getDisplayName() {
            return getName();
        }

        /**
         * Return the icon to display.
         * @return The projects icon.
         */
        public Icon getIcon() {
            return new ImageIcon(Utilities.loadImage("de/uniwuerzburg/informatik/mindmapper/project/lightbulb.png"));
        }

        /**
         * Return the project itself.
         * @return The project.
         */
        public Project getProject() {
            return MindMapProject.this;
        }

        /**
         * Add a property change listener.
         * @param listener The listener to add.
         */
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }

        /**
         * Remove a property change listener.
         * @param listener The listener to remove.
         */
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            support.removePropertyChangeListener(listener);
        }
    }

    /**
     * Properties which will mark the project state as modified if the
     * properties change.
     */
    protected final class NotifyProperties extends Properties {
        /**
         * The project state to notify.
         */
        protected final ProjectState state;

        /**
         * Create a properties instance which will notify the project state on
         * changes.
         * @param state The project state to notify.
         */
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

    /**
     * The logical view for this project.
     */
    protected final class MindMapLogicalView implements LogicalViewProvider {

        /**
         * The project displayed in this view.
         */
        protected MindMapProject project;

        /**
         * Create a view for this project.
         * @param project The project to create a view for.
         */
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

        /**
         * The nodes used to display mindmap documents in the project explorer.
         */
        private class MindMapsNode extends FilterNode {
            /**
             * The project which includes this mindmap document.
             */
            protected MindMapProject project;

            /**
             * Create a new node delegating to the given node for this project.
             * @param node The node to delegate to.
             * @param project The project owning the node.
             */
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
