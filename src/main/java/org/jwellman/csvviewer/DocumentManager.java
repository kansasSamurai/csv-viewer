package org.jwellman.csvviewer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 
 * @author Rick Wellman
 *
 */
public class DocumentManager extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedPane;
	
	public DocumentManager() {
		super(new BorderLayout());
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("File1", new DataBrowser());
		tabbedPane.addTab("File2", new DataBrowser());
		
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
}
