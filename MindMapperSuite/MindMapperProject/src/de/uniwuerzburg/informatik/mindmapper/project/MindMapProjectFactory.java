/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniwuerzburg.informatik.mindmapper.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author blair
 */
public class MindMapProjectFactory implements ProjectFactory{

    public static final String projectDirectory = "mmproject";
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
            throw new IOException("Project directory " + project.getProjectDirectory() + "deleted, can not save project.");
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
