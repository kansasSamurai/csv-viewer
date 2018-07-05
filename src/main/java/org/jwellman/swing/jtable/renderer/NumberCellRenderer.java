package org.jwellman.swing.jtable.renderer;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.jwellman.swing.font.FontFactory;

/**
 * A renderer for numeric String(s). Right aligns the data.
 * 
 * Note:  It does not make sense to override this class and change the alignment;
 * if that is your goal, override xxx instead.
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
    	if (font != null) this.setFont(font);
    	setHorizontalAlignment(SwingConstants.RIGHT);	
	}
	
	/**
	 * @deprecated this will go away soon
	 * 
	 * @param fontname
	 * @param notused a parameter to simply disambiguate the constructors to allow a no-args constructor
	 */
	public NumberCellRenderer(String fontname, String notused) {
		this(FontFactory.getFont(fontname, Font.PLAIN, 12));
	}
	
    // For now, lets not provide a getter; i.e. why would you want it? you set it.
	//	public Border getBorder() {
	//		return border;
	//	}

    /**
     * Provide a Border around the rendered Component.
     * 
     * Though it could be used for other purposes, its main usage is to help fix
     * visual alignment within the cell.  IMO, JTable's native implementation tends
     * to put text too ... 
     * 
     * Also, if you intend to have highlighted rows and do not want the visual artifact
     * created by JTable's default value of xxx
     * then you will also need to use a Border to help provide visual separation 
     * between Cell contents.
     */
	public void setBorder(Border border) {
		this.border = border;
	}

	/**
	 * A convenience method to set an empty border since that is the most common use case.
	 * 
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 */
	public void setBorder(int top, int left, int bottom, int right) {
		this.border = BorderFactory.createEmptyBorder(top, left, bottom, right);
	}

}
