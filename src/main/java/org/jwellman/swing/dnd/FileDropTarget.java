package org.jwellman.swing.dnd;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;

import org.jwellman.swing.actions.FileActionAware;

/**
 * Simple Drop Target inspired by
 * https://java-demos.blogspot.com/2013/06/drag-and-drop-file-in-jtextarea.html 
 * 
 * Drop File:
 * https://www.flaticon.com/free-icon/inbox_3685
 * https://www.flaticon.com/free-icon/outbox_3686
 * <div>Icons made by <a href="https://www.flaticon.com/authors/daniel-bruce" title="Daniel Bruce">Daniel Bruce</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
 * Compress/Pack: https://www.flaticon.com/free-icon/compress_150498
 * <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
 * Exchange/Unpack: https://www.flaticon.com/free-icon/exchange_150531
 * <div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div> 
 * 
 * @author Rick
 *
 */
public class FileDropTarget extends DropTargetAdapter {

	private JComponent target;
	
	private FileActionAware handler;
	
	private Color originalBackground;
	
	private Color dragOverColor = new Color(0x393939); // (0xc8dadf);
	
	public FileDropTarget(JComponent component, FileActionAware aware) {
		super();
		this.target = component;
		this.handler = aware;
		this.originalBackground = this.target.getBackground();
	}
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		this.target.setBackground(dragOverColor);
	} 
	
	@Override
	public void dragExit(DropTargetEvent dtde) {
		this.target.setBackground(originalBackground);
	}
	
	@Override
	public void drop(DropTargetDropEvent e) {

		this.target.setBackground(originalBackground);

		// Accept the drop first, important!
        e.acceptDrop(DnDConstants.ACTION_COPY);
		
		try {
            
            // Get the files that are dropped as java.util.List
            @SuppressWarnings( "unchecked" )
			final List<File> list = (List<File>) e
				.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            
            if ( list.size() == 1) {
                // Now get the first/only file from the list,                
                final File file = (File)list.get(0);
                System.out.println(file);
                
                this.handler.doSingleFileAction(file);            	
            } else {
            	this.handler.doListOfFilesAction(list);
            }            
            
        } catch (Exception ex) {
            // java.awt.datatransfer.UnsupportedFlavorException
        	ex.printStackTrace();
        }
		
	}

}
