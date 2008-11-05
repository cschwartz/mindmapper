package de.uniwuerzburg.informatik.mindmapper.api;

import org.openide.filesystems.FileObject;

/**
 * A factory for creating new mindmaps, saving them to and from FileObjects.
 * @author Christian "blair" Schwartz
 */
public interface MindMapFactory {
    /**
     * Creates a new Document instance.
     * @return A new document.
     */
    public Document createDocument();

    /**
     * Load a Document from the given FileObject instance.
     * @param fileObject The FileObject to load from.
     * @return The loaded document.
     */
    public Document loadDocument(FileObject fileObject);

    /**
     * Save to an existing FileObject.
     * @param fileObject The FileObject to save to.
     * @param document The document to save.
     */
    public void saveDocument(FileObject fileObject, Document document);
}
