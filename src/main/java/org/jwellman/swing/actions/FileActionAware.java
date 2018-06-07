package org.jwellman.swing.actions;

import java.io.File;
import java.util.List;

/**
 * A simple interface used as a callback for any type of action
 * related to a file or list of files.
 * 
 * @author rwellman
 *
 */
public interface FileActionAware {

    public void doSingleFileAction(File file);
    
    public void doListOfFilesAction(List<File> listoffiles);
    
}
