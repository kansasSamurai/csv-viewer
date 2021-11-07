package org.jwellman.swing.jtable.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A base class to capture common cell renderer features:
 * <p><ul>
 * <li>Add a default Border to all subclass instances</li>
 * <li>Add a custom  Border to any subclass instance</li>
 * </ul>
 * 
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public class AbstractTableCellRenderer extends DefaultTableCellRenderer {

	protected Font customFont;
	
    protected Border cellBorder;
    
    protected static final Border BORDER_FIX = BorderFactory.createEmptyBorder(6, 3, 0, 3); // 5, 3, 0, 3 | 5, 1, 0, 1   |   6, 0, 1, 0
    
    protected static final Border BORDER_OUTLINE = BorderFactory.createLineBorder(Color.red, 1);
    
    protected static final Border BORDER_COMPOUND = BorderFactory.createCompoundBorder( BORDER_FIX, BORDER_OUTLINE );
    
    protected AbstractTableCellRenderer(Font font) {
    	if (font != null) this.customFont = font;
    }
    
	@Override
    public Component getTableCellRendererComponent(
    		JTable table, Object value,
            boolean isSelected, boolean hasFocus, 
            int row, int column) {
		
    	final JComponent c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	c.setBorder(this.cellBorder == null ? BORDER_FIX : this.cellBorder);
    	//if (this.customFont != null) c.setFont(this.customFont); 
    	
    	this.postProcess(c);
    	
    	return c;
	}

	/**
	 * Ability to further process the JComponent returned from the original Swing implementation
	 * after we have applied our own border (if any) to it.
	 * 
	 * @param c
	 */
	protected void postProcess(JComponent c) {
		// An empty default implementation.
	}	

	/**
	 * For now, lets not provide a getter; 
	 * i.e. why would you want it? you set it.
	 * 
	 * Finally decided that one possible use case is if
	 * you set the border using the insets method and later
	 * need to retrieve the border in a separate piece of code
	 * (like in an Action).
	 * 
	 * Be careful... I originally misnamed this "getBorder()"
	 * which overrides the JComponent method and can (and did)
	 * have the effect of "undoing" the border set in getTableCellRendererComponent().
	 * 
	 * @return a Border object for the renderer; may be null
	 */
	public Border getCellBorder() {
		return this.cellBorder;
	}

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
	public void setCellBorder(Border border) {
		this.cellBorder = border;
	}

	/**
	 * A convenience method to set an empty border since that is the most common use case.
	 * 
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 */
	public void setCellBorder(int top, int left, int bottom, int right) {
		this.cellBorder = BorderFactory.createEmptyBorder(top, left, bottom, right);
	}

}
