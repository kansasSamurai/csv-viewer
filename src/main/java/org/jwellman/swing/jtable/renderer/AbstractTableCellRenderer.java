package org.jwellman.swing.jtable.renderer;

import java.awt.Component;

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

    protected Border border;
    
    private static final Border BORDER_FIX = BorderFactory.createEmptyBorder(6, 0, 0, 0);

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
		
    	final JComponent c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	c.setBorder(this.border == null ? BORDER_FIX : this.border);
    	
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

}
