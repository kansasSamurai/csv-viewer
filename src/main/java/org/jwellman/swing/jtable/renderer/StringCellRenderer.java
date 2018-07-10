package org.jwellman.swing.jtable.renderer;

import java.awt.Font;

import javax.swing.SwingConstants;

/**
 * A renderer for plain ole String(s). Left aligns the data.
 * 
 * @author rwellman
 *
 */
public class StringCellRenderer extends AbstractTableCellRenderer {

	private static final long serialVersionUID = 1L;

	public StringCellRenderer() {
		this(null);
	}
	
	public StringCellRenderer(Font font) {
    	super(font);
    	setHorizontalAlignment(SwingConstants.LEFT);	
	}
	
}
