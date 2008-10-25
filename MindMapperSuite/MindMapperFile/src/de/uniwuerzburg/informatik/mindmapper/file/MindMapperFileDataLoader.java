/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniwuerzburg.informatik.mindmapper.file;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class MindMapperFileDataLoader extends UniFileLoader {

    public static final String REQUIRED_MIME = "text/x-mindmapper+xml";
    private static final long serialVersionUID = 1L;

    public MindMapperFileDataLoader() {
        super("de.uniwuerzburg.informatik.mindmapper.file.MindMapperFileDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(MindMapperFileDataLoader.class, "LBL_MindMapperFile_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new MindMapperFileDataObject(primaryFile, this);
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
