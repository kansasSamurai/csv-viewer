package org.jwellman.swing.jtable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class JTablePropertyAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
	private JTable target;
	
    public JTablePropertyAction(String text, JTable table) {
        super(text);
        this.target = table;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        target.clearSelection();
        
//        JCheckBox cbLog = (JCheckBox) e.getSource();
//        if (cbLog.isSelected()) {
//            System.out.println("Logging is enabled");
//        } else {
//            System.out.println("Logging is disabled");
//        }
    }
    
}
