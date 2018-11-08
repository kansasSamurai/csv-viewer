package org.jwellman.swing.jtable;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;

/**
 * An extension of JTable that supports row striping and rollover effects.
 * 
 * References:
 * Highlight JTable Rows on Rollover
 * https://community.oracle.com/thread/1389010?start=0&tstart=0 
 * 
 * @author rwellman
 *
 */
public class XTable extends JTable implements MouseInputListener {

	private static final long serialVersionUID = 1L;

    private int rollOverRowIndex;
    
    private Color stripe = new Color(249,249,249);
    
    private Color rolloverBackground = new Color(0xDEDEDE);

    public XTable() {
        super();
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);

		if (isRowSelected(row)) {
            c.setForeground(this.getSelectionForeground());
            c.setBackground(this.getSelectionBackground());
		} else {
		    c.setForeground(this.getForeground());

		    // Alternate row color
            c.setBackground(this.getBackground());
	        if (this.isRowStripingEnabled() && row % 2 == 0) {
	            c.setBackground( stripe );               
	        }
		}

        // Mouse Rollover (trumps row striping)
        if (this.isRolloverEnabled() && row == rollOverRowIndex) {
            c.setBackground(rolloverBackground);
        }

		return c;
	}

    private boolean isRolloverEnabled() {
        return true;
    }

    private boolean isRowStripingEnabled() {
        return true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.print("p");        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.print("r");
        this.mouseMoved(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        rollOverRowIndex = -1;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // System.out.print("d");
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.print("m");
        int row = rowAtPoint(e.getPoint());
        if ( row != rollOverRowIndex ) {
            rollOverRowIndex = row;
            repaint();
        }
   }

}
