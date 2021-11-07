package org.jwellman.swing.jtable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.jwellman.csvviewer.Settings;
import org.jwellman.swing.icon.ColorIcon ;
import org.jwellman.swing.icon.CompositeIcon;
import org.jwellman.swing.jtable.renderer.NumberCellRenderer;
import org.jwellman.swing.jtable.renderer.StringCellRenderer;
import org.jwellman.swing.mouse.RubberBandingListener;

/**
 * An extension of JTable that supports row striping and rollover effects.
 * 
 * TODO move this to a standalone JAR so that other projects can use it
 *      ... this will require a refactor to remove the following dependencies: 
 *      RubberBandingListener 
 *      jiconfont.*
 *      CompositeIcon
 * TODO the standalone jar will require the following classes:
 *     NumberCellRenderer
 *     StringCellRenderer
 *     
 * References:
 * Highlight JTable Rows on Rollover
 * https://community.oracle.com/thread/1389010?start=0&tstart=0 
 * 
 * @author rwellman
 *
 */
public class XTable extends JTable implements MouseInputListener, SwingConstants {

    private static final long serialVersionUID = 1L;

    private static final int ICONSIZE = 13;

    public static final Font FONT_GRID = new Font("Segoe UI", Font.PLAIN, 36); // new Font("Consolas", Font.PLAIN, 14);

    public static final Font FONT_SEGOE_UI = new Font("Segoe UI", Font.PLAIN, 14);

    public static final Font FONT_SEGOE_UI_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public static final Color COLOR_STRIPE = new Color(240,240,240); // Color(249,249,249); // VERY LIGHT GRAY

    public static final Color COLOR_GRID = new Color(218,218,218); // Color(249,249,249); // VERY LIGHT GRAY

    public static final Color COLOR_ROLLOVER = new Color(0xDEDEDE); // LIGHT GRAY

    // This is the color that icons/decorations are painted during rollover
    public static final Color COLOR_ROLLOVER_HIGHLIGHT = new Color(0xDCDCDC); // "GRAY" << NOTICE... barely darker than rollover color >> 0xBDBDBD 0xC7C7C7 0xD5D5D5 < these are ok but maybe better

	private static final Color COLOR_GREY_DARKEST = new Color(64,64,64);

    public static final Color COLOR_SELECTION_BLUE = new Color(0x3A87AD) ; // royal blue

    public static final Color COLOR_SELECTION_LIGHTBLUE = new Color(168,214,225); // new Color(0xD9EDF7) ; // sky blue

    public static final NumberCellRenderer numRenderer = new NumberCellRenderer(FONT_GRID);

    public static final StringCellRenderer strRenderer = new StringCellRenderer(FONT_GRID);

    // ====================================================================

    private int rollOverRowIndex;

    private boolean rolloverEnabled = true;

    private boolean rowStripingEnabled = true;

    private Border debugcellborder = BorderFactory.createLineBorder(Color.cyan);

    private RubberBandingListener rbandListener = new RubberBandingListener();

    @SuppressWarnings("unused")
    private Icon bookmark = IconFontSwing.buildIcon(FontAwesome.BOOKMARK, ICONSIZE, COLOR_ROLLOVER_HIGHLIGHT);
    
    private Icon bugFixed = IconFontSwing.buildIcon(FontAwesome.BUG, ICONSIZE, new Color(0x106022));
    //IconFontSwing.buildIcon(FontAwesome.BUG, ICONSIZE, new Color(0x106022));
    //IconFontSwing.buildIcon(FontAwesome.USER_CIRCLE, 16, new Color(138, 109, 182) );

    private Icon bugCritical = IconFontSwing.buildIcon(FontAwesome.BUG, ICONSIZE, new Color(0x801F15));
    
    private Icon date = IconFontSwing.buildIcon(FontAwesome.CALENDAR_O, ICONSIZE, COLOR_ROLLOVER_HIGHLIGHT);
    
    private Icon swatch = new ColorIcon(new Color(0x106022), 11);
    
    private CompositeIcon decOne = new CompositeIcon();
    
    private CompositeIcon decTwo = new CompositeIcon();

    public XTable() {
        super();
        init();
    }

    public XTable(TableModel tm, TableColumnModel tcm) {
        super(tm, tcm);
        init();
    }

    private void init() {

        this.setGridColor(COLOR_GRID);

        this.setForeground(COLOR_GREY_DARKEST); //(COLOR_ROLLOVER_HIGHLIGHT);
        this.setBackground(Color.WHITE); 

        this.setSelectionForeground(COLOR_GREY_DARKEST); // ( COLOR_SELECTION_BLUE );
        this.setSelectionBackground( COLOR_SELECTION_LIGHTBLUE );

        this.getTableHeader().setFont(FONT_SEGOE_UI_BOLD); // (FONT_CALIBRI_BOLD)
// 10/31/2021 : Allow look and feel defaults for font color/background (but leaving the code... I might change my mind again)
//        this.getTableHeader().setForeground(COLOR_GREY_DARKEST);
//        this.getTableHeader().setBackground(COLOR_ROLLOVER);

        // ===========================================================
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        this.addMouseListener(rbandListener);
        this.addMouseMotionListener(rbandListener);
        
        int top = 2;
        decOne.setMargin(new Insets(0, 3, 0, 3));
        decOne.setHorizontalGap(5);
//        decOne.addIcon(bookmark, top);
//        decOne.addIcon(bugCritical, top);
        decOne.addIcon(date, top); // top-1
        
        top = 4;
        decTwo.setMargin(new Insets(0, 3, 0, 3));
        decTwo.setHorizontalGap(1); 
            ((ColorIcon)swatch).setMargin(new Insets(0,0,0,2));
        decTwo.addIcon(swatch, top+1); 
        decTwo.addIcon(bugCritical, top);
        decTwo.addIcon(bugFixed, top);
        //decTwo.addIcon(date, top); // top-1
        // ((ColorIcon)swatch).setRoundedCorners(true);
    }

    /**
     * Thanks to:
     * https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
     * 
     */
    public void resizeColumnWidth() {
        final TableColumnModel columnModel = this.getColumnModel();
        for (int column = 0; column < this.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < this.getRowCount(); row++) {
                TableCellRenderer renderer = this.getCellRenderer(row, column);
                Component comp = this.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            //If using org.jwellman.utility, then you can use the following line:
            //width = Limit.rangeOf(width).toRange(50,300); 
            if(width > 300) width=300;
            if(width < 50) width=50;
            
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    /**
     * TODO The icon customization needs to be removed; at best, maybe add
     * some sort of a callback for the icons but xtable by itself
     * should not implement this.
     */
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

		boolean debug = false;

		final Component c = super.prepareRenderer(renderer, row, column);
		
		final JLabel label = (c instanceof JLabel) ? (JLabel)c : null; 
		
		if ( this.isCellSelected(row, column) ) {
            c.setForeground(this.getSelectionForeground());
            c.setBackground(this.getSelectionBackground());
		} else {
		    c.setForeground(this.getForeground());

		    // Alternate row color
            c.setBackground(this.getBackground());
	        if (this.isRowStripingEnabled() && row % 2 == 0) {
	            c.setBackground( COLOR_STRIPE );               
	        } else {
	            // This is actually important for two reasons:
	            // 1. Just like the stripe color, I am setting the color
	            //    of all table rows in a very opinionated manner.
	            // 2. A very few look and feels (such as Napkin)
	            //    actual use transparency by default which not only
	            //    does not look very good (despite my love for Napkin in
	            //    pretty much every other way), but... see point #1 :)
	            c.setBackground(Color.white);
	        }
		}

        // Mouse Rollover (trumps row striping)
        if (this.isRolloverEnabled() && row == rollOverRowIndex) {
            c.setBackground(COLOR_ROLLOVER);
        }

        //
        if (label != null) {
        	
        	if (Settings.global().isUserMode()) {

        	} else {
            	// This does not look right when there is an icon :(
            	// label.setVerticalAlignment(BOTTOM);
            	// TODO figure this out.
            	// So... I don't think this can be fixed by just using
            	// a JLabel/DefaultCellRenderer.  Because... any time
            	// there is an icon in a JLabel, it centers the
            	// text and icon horizontally 
            	// (regardless of the vertical alignment setting)
            	
                CompositeIcon icon = null;
            	column = this.convertColumnIndexToModel(column); //this.convertColumnIndexToView(column);
            	//column = -1;
            	switch (column) {
                    case 3: icon = decTwo; icon.setLabel(label); break;
                    case 6: icon = decOne; icon.setLabel(label); break;
                    default: label.setIcon(null);
            	}
        	}

        }

        if (debug) {
            if (c instanceof JComponent) {
                ((JComponent) c).setBorder(debugcellborder);
            }            
        }

		return c;
	}

	public void setRolloverEnabled(boolean enabled) {
	    this.rolloverEnabled = enabled;
	}

	public boolean isRolloverEnabled() {
        return this.rolloverEnabled;
    }

	public void setRowStripingEnabled(boolean enabled) {
	    this.rowStripingEnabled = enabled;
	}

    public boolean isRowStripingEnabled() {
        return this.rowStripingEnabled;
    }

    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	final Graphics2D g2 = (Graphics2D)g.create(); {
    		rbandListener.paint(g2, Color.MAGENTA, Color.BLUE);
    	} g2.dispose();
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

    @Override
    public void addNotify() {
        // System.out.println("jtable addnotify");
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
    
}
