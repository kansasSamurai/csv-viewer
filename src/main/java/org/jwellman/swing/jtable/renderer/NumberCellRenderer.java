package org.jwellman.swing.jtable.renderer;

import java.awt.Font;

import javax.swing.SwingConstants;

import org.jwellman.swing.font.FontFactory;

/**
 * A renderer for numeric String(s). Right aligns the data.
 * 
 * Note:  It does not make sense to override this class and change the alignment;
 * if that is your goal, override AbstractTableCellRenderer instead.
 * 
 * @author rwellman
 *
 */
public class NumberCellRenderer extends AbstractTableCellRenderer  {

	private static final long serialVersionUID = 1L;

	public NumberCellRenderer() {
		this(null);
	}

	public NumberCellRenderer(Font font) {
		super(font);
		setHorizontalAlignment(SwingConstants.RIGHT);	
	}

	/**
	 * Sets the font based on the given name.
	 * It will use a PLAIN font sized at 12 points.
	 * 
	 * 
	 * @deprecated this will go away soon
	 * 
	 * @param fontname
	 * @param notused a parameter to simply disambiguate the constructors to allow a no-args constructor
	 */
	public NumberCellRenderer(String fontname, String notused) {
		this(FontFactory.getFont(fontname, Font.PLAIN, 12));
	}

}
