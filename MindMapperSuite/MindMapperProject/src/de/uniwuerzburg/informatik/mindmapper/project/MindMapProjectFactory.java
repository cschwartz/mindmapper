package de.uniwuerzburg.informatik.mindmapper.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;

/**
 * A factory for creating MindMap Projects.
 * @author Christian "blair" Schwartz
 */
public class MindMapProjectFactory implements ProjectFactory{

    /**
     * The name of the directory containing the properties file.
     */
    public static final String projectDirectory = "mmproject";

    /**
     * The name of the project properties file.
     */
    public static final String projectFile = "project.properties";

    public boolean isProject(FileObject directory) {
        return directory.getFileObject(projectDirectory) != null;
    }

    public Project loadProject(FileObject directory, ProjectState state) throws IOException {
        return isProject(directory) ? new MindMapProject(directory, state) : null;
    }

    public void saveProject(Project project) throws IOException, ClassCastException {
        FileObject projectDir = project.getProjectDirectory();
        if(projectDir.getFileObject(projectDirectory) == null) {

            throw new IOException(NbBundle.getMessage(MindMapProjectFactory.class, "projectdirectory") + project.getProjectDirectory() + NbBundle.getMessage(MindMapProject.class, "projectSaveError"));
        }
        
        String propertiesPath = projectDirectory + "/" + projectFile;
        FileObject propertiesFile = projectDir.getFileObject(propertiesPath);
        if(propertiesPath == null) {
            propertiesFile = projectDir.createData(propertiesPath);
        }
        
        ((MindMapProject)project).getMindMapDirectory(true);
        
        Properties properties = project.getLookup().lookup(Properties.class);
        
        File f = FileUtil.toFile(propertiesFile); 
        properties.store(new FileOutputStream(f), "MindMap Project File");
    }

}
