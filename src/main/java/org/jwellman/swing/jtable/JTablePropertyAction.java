package org.jwellman.swing.jtable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class JTablePropertyAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
    private int action;
    
    private String columnName;
    
	private JTable target;

	public static final int ACTION_HIDE_COLUMN = 22;
    public static final int ACTION_SHOW_COLUMN = 23;
	public static final int ACTION_CLEAR_SELECTION = 55;
	public static final int ACTION_TOGGLE_AUTORESIZEMODE = 66;
	public static final int ACTION_TOGGLE_COLUMNSELECTION = 77;
	
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
            default:
                System.out.println("UNDEFINED Action identifier: " + action);
            }
            
        }

    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        TableColumn column = null;
        
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
