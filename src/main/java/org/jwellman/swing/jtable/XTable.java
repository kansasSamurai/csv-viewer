package org.jwellman.swing.jtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

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
    
    private Border debugcellborder = BorderFactory.createLineBorder(Color.cyan);

    public XTable() {
        super();
        init();
    }
    
    public XTable(TableModel tm, TableColumnModel tcm) {
        super(tm, tcm);
        init();
    }
    
    private void init() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);        
    }

    @Override
    public void addNotify() {
        System.out.println("jtable addnotify");
        super.addNotify();
    }

    @Override
    protected void configureEnclosingScrollPane() {
        
        boolean useJTableImpl = true;        
        if (useJTableImpl) {
            super.configureEnclosingScrollPane();
        } else {
            /* I am not using this but do not delete...
             * This is a proof of concept for adding components to the scrollpane header and I want to capture my 
             * thoughts and results.  This works pretty good but there are some things to note.
             * 1) The width of the header viewport is controlled by the total width of the jtable columns
             *    !! when NOT in auto resize mode !!  What this means is that if the jtable columns are not at
             *    a very minimum size (i.e. zero?) then resizing your jframe (which resizes your scrollpane) horizontally
             *    will usually result in a clipping of your custom content on the right (which is probably undesirable)
             */
            Container parent = SwingUtilities.getUnwrappedParent(this);
            if (parent instanceof JViewport) {
                JViewport port = (JViewport) parent;
                Container gp = port.getParent();
                if (gp instanceof JScrollPane) {
                    final JScrollPane scrollPane = (JScrollPane)gp;
                    
                    // Make certain we are the viewPort's view and not, for
                    // example, the rowHeaderView of the scrollPane -
                    // an implementor of fixed columns might do this.
                    JViewport viewport = scrollPane.getViewport();
                    if (viewport == null || SwingUtilities.getUnwrappedView(viewport) != this) {
                        return;
                    }
                    
                    JPanel p = new JPanel(new BorderLayout());
                    p.add(new JButton("Click Me"), BorderLayout.NORTH);
                    p.add(getTableHeader(), BorderLayout.SOUTH);
                    p.add(new JButton("E"), BorderLayout.EAST);
                    p.add(new JButton("W"), BorderLayout.WEST);
                    p.add(new JButton("Click Me"), BorderLayout.CENTER);
                                    
                    // scrollPane.setColumnHeaderView(getTableHeader()); // original JTable implementation
                    // superceded by this...
                    scrollPane.setColumnHeaderView(p);
                    
                    // This is in JTable but the method is private :(
                    // configure the scrollpane for any LAF dependent settings
                    // configureEnclosingScrollPaneUI();
                }
            }            
        }
    }
    
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		boolean debug = false;

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
        
        if (debug) {
            if (c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                ((JComponent) c).setBorder(debugcellborder);
            }            
        }

		return c;
	}

    // TODO provide setter
    private boolean isRolloverEnabled() {
        return true;
    }

    // TODO provide setter
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
