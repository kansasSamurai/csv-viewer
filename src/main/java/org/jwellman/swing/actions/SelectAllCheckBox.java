package org.jwellman.swing.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

/**
 * This action is intended to be attached to a JCheckBox (via the constructor).
 * 
 * The state of the JCheckBox, when clicked, will be reflected into all JCheckBox
 * that have previously been "registered" via the register() method.
 * 
 * If you want the registered JCheckBox to "do something" when this action
 * changes its state, then you must create and add an ItemListener.
 *  
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public class SelectAllCheckBox extends AbstractAction implements Runnable {

	private AbstractButton control;
	
	private List<JCheckBox> listOfBoxes = new ArrayList<>();
	
	public SelectAllCheckBox(AbstractButton b) {
		this.control = b;
		this.control.addActionListener(this);
	}
	
	public JCheckBox register(JCheckBox abox) {
		this.listOfBoxes.add(abox);
		return abox;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(this);
	}

	@Override
	public void run() {
		for (JCheckBox abox : listOfBoxes) {
			if (abox.isSelected() == control.isSelected()) {
				// do nothing; abox already matches the control
			} else {
				abox.setSelected(control.isSelected());
				//SwingUtilities.invokeLater(new DoClick(abox));
			}
		}
	}

	// This works but as the comment in the run() method indicates,
	// using doClick is "slow" because it emulates a "mouse press time".
	// By simply setting the "selected" property as we do above,
	// we rely on an ItemListener to implement the "action" logic.
	private class DoClick implements Runnable {

		private JCheckBox jcheckbox;
		
		public DoClick(JCheckBox abox) {
			this.jcheckbox = abox;
		}

		@Override
		public void run() {
			jcheckbox.doClick(); // this is slow but works
		}
		
	}
	
}
