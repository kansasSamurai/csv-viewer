package org.jwellman.swing.jtable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.jwellman.utility.Limit;

/**
 * A class that encapsulates access to all properties of a JTable.
 * 
 * @author rwellman
 *
 */
public class JTablePropertyAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
    private int action;
    
    private String columnName;
    
	private JTable target;

	public static final int ACTION_HIDE_COLUMN = 22;
    public static final int ACTION_SHOW_COLUMN = 23;
    public static final int ACTION_TOGGLE_GRID = 24;
    public static final int ACTION_TOGGLE_VERTICAL_LINES = 25;
    public static final int ACTION_TOGGLE_HORIZONTAL_LINES = 26;
	public static final int ACTION_CLEAR_SELECTION = 55;
	public static final int ACTION_TOGGLE_AUTORESIZEMODE = 66;
	public static final int ACTION_INCREASE_COLUMN_MARGIN = 71;
	public static final int ACTION_DECREASE_COLUMN_MARGIN = 72;
	public static final int ACTION_INCREASE_ROW_MARGIN = 73;
	public static final int ACTION_DECREASE_ROW_MARGIN = 74;
	public static final int ACTION_INCREASE_ROW_HEIGHT = 75;
	public static final int ACTION_DECREASE_ROW_HEIGHT = 76;
	public static final int ACTION_TOGGLE_COLUMNSELECTION = 77;

	/**
	 * 
	 * @param text
	 * @param table
	 * @param actionid
	 * @param cname originally intended for show/hide column but currently unused in preference of TableColumnManager
	 */
    public JTablePropertyAction(String text, JTable table, int actionid, String cname) {
        super(text);
        assert(table != null);
        assert(actionid > 0);
        
        this.target = table;
        this.action = actionid;
        this.columnName = cname;

        if (target != null) {
            switch (this.action) {
            case ACTION_HIDE_COLUMN:
                break;
            case ACTION_CLEAR_SELECTION:
                break;
            }
            
        }

    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        
    	int i = 0;
    	
        TableColumn column = null; // Reusable TableColumn reference
        
        AbstractButton button = null; // A lot of actions/properties can come only from buttons
        
        if (target != null) {
            switch (this.action) {
            case ACTION_HIDE_COLUMN:
                column = target.getColumn(columnName);
                column.setWidth(0); column.setMaxWidth(0); column.setMinWidth(0);                
                break;
            case ACTION_SHOW_COLUMN:
                column = target.getColumn(columnName);
                column.setWidth(50); column.setMaxWidth(50); column.setMinWidth(50);                
                break;
            case ACTION_CLEAR_SELECTION:
                target.clearSelection();
                break;
            case ACTION_TOGGLE_AUTORESIZEMODE:
                final boolean isoff = target.getAutoResizeMode() == JTable.AUTO_RESIZE_OFF;
                target.setAutoResizeMode(isoff ? JTable.AUTO_RESIZE_ALL_COLUMNS : JTable.AUTO_RESIZE_OFF);
                break;
            case ACTION_TOGGLE_GRID:
                button = (AbstractButton) e.getSource();
                target.setShowGrid(button.isSelected());
                break;
            case ACTION_TOGGLE_HORIZONTAL_LINES:
                target.setShowHorizontalLines(!target.getShowHorizontalLines());
                break;
            case ACTION_TOGGLE_VERTICAL_LINES:
                target.setShowVerticalLines(!target.getShowVerticalLines());
                break;
            case ACTION_INCREASE_ROW_MARGIN:
            	i = target.getRowMargin();
            	target
            	  .setRowMargin(Limit.incrementOf(i).to(100));
            	break;
            case ACTION_DECREASE_ROW_MARGIN:
            	i = target.getRowMargin();
            	target
            	  .setRowMargin(Limit.decrementOf(i).to(0));
            	break;
            case ACTION_INCREASE_COLUMN_MARGIN:
            	i = target.getColumnModel().getColumnMargin();
            	target
            	  .getColumnModel()
            	    .setColumnMargin(Limit.incrementOf(i).to(100));
            	
            	break;
            case ACTION_DECREASE_COLUMN_MARGIN:
            	i = target.getColumnModel().getColumnMargin();
            	target
            	  .getColumnModel()
            	    .setColumnMargin(Limit.decrementOf(i).to(0));
            	break;
            case ACTION_INCREASE_ROW_HEIGHT:
            	i = target.getRowHeight();
            	target.setRowHeight(++i);
            	break;
            case ACTION_DECREASE_ROW_HEIGHT:
            	i = target.getRowHeight();
            	target.setRowHeight(--i);
            	break;
            case ACTION_TOGGLE_COLUMNSELECTION:
            	final boolean ison = target.getColumnSelectionAllowed();
            	target.setColumnSelectionAllowed(!ison);
            	break;
            default:
                System.out.println("UNDEFINED Action identifier: " + action);
            }
            
        } else {
            System.out.println("Action called on NULL target: " + action);
        }
        
    }
    
}
