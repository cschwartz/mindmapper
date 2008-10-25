package de.uniwuerzburg.informatik.mindmapper.file.actions;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author blair
 */
public abstract class AbstractUndoableAction implements UndoableEdit{
    public void die() {
        
    }

    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    public boolean replaceEdit(UndoableEdit anEdit) {
        return true;
    }

    public boolean isSignificant() {
        return true;
    }

}
