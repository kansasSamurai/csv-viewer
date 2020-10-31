package org.jwellman.csvviewer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jwellman.csvviewer.glazed.DataTextFilterator;

/**
 * An action that adds/removes a column to the DataTextFilterator.
 * 
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public class ActionSetFilterColumn extends AbstractAction implements Runnable {

	private JCheckBox checkbox;
	
	private JTextField textfield;
	
	private DataTextFilterator filterator;
	
	private int index;
	
	public ActionSetFilterColumn(JCheckBox c, JTextField t, DataTextFilterator dtf, int i) {
		this.index = i;
		this.checkbox = c;
		this.textfield = t;
		this.filterator = dtf;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(this);
	}

	@Override
	public void run() {
		if (this.checkbox.isSelected()) {
			filterator.add(this.index);
		} else {
			filterator.remove(this.index);
		}
		
		// Update the "search" textfield to "trigger" the table update.
		SwingUtilities.invokeLater(new DoSetText(this.textfield));
	}
	
	/**
	 * I almost forgot why I implemented this...
	 * This is to "trigger" the setText() on the textfield which will in turn
	 * "trigger" the table to "update" (possibly) based on the new indices
	 * that were modified via filterator.add()/remove() in the run() method.
	 * 
	 * This seems like a "hack" so I'm not sure if this is the cleanest
	 * implementation I could have come up with.  But it works so leaving it alone for now.
	 *
	 */
	private class DoSetText implements Runnable {

		private JTextField jtextfield;
		
		public DoSetText(JTextField textfield) {
			this.jtextfield = textfield;
		}

		@Override
		public void run() {
			jtextfield.setText(jtextfield.getText());
		}
		
	}

}
