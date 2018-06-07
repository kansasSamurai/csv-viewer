package org.jwellman.swing.jtable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class JTablePropertyAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
    private int action;
    
	private JTable target;

	public static final int ACTION_HIDE_COLUMN = 22;
	public static final int ACTION_CLEAR_SELECTION = 55;
	
    public JTablePropertyAction(String text, JTable table, int actionid) {
        super(text);
        this.target = table;
        this.action = actionid;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (target != null) {
            switch (this.action) {
            case ACTION_HIDE_COLUMN:
                break;
            case ACTION_CLEAR_SELECTION:
                target.clearSelection();
                break;
            default:
                System.out.println("UNDEFINED Action identifier: " + action);
            }
            
        } else {
            System.out.println("Action called on NULL target: " + action);
        }
        
//        JCheckBox cbLog = (JCheckBox) e.getSource();
//        if (cbLog.isSelected()) {
//            System.out.println("Logging is enabled");
//        } else {
//            System.out.println("Logging is disabled");
//        }
    }
    
}
