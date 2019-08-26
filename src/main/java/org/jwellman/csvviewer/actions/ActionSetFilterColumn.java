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
		//JOptionPane.showMessageDialog(this.component, "Your action works");
		if (this.checkbox.isSelected()) {
			filterator.add(this.index);
		} else {
			filterator.remove(this.index);
		}
		SwingUtilities.invokeLater(new DoSetText(this.textfield));
	}
	
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
