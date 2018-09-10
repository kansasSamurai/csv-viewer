package org.jwellman.csvviewer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import org.jwellman.csvviewer.interfaces.TextChooserAware;
import org.jwellman.foundation.swing.XToggleButton;
import org.jwellman.swing.component.HorizontalGraphitePanel;

public class DelimiterChooser implements TextChooserAware {

	private String chosenText = ",";
	
	private JComponent userinterface;

	public DelimiterChooser() {

        final Dimension d = new Dimension(22, 1);
        final ButtonGroup bg = new ButtonGroup(); 
        this.userinterface = HorizontalGraphitePanel.createDefault(Arrays.asList(
                 HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector(",")).addTo(bg).setSelected(true).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("~")).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("|")).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("\\t")).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("<?>")).addTo(bg).get(), null, d)
            ));
        
	}
	
	@Override
	public String getText() {
		return this.chosenText;
	}
	
	public JComponent getUI() {
	    return this.userinterface;
	}

    @SuppressWarnings("serial")
    private class DelimiterSelector extends AbstractAction {
        
        public DelimiterSelector(String s) {
            super(s);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof AbstractButton) {
                final AbstractButton b = (AbstractButton)e.getSource();
                final String command = b.getActionCommand();
                if (command.equals("<?>")) {
                    // TODO implement popup/dialog for custom text
                } else {                    
                    chosenText = command;
                    System.out.println("User selected: " + chosenText);
                }
                                
            }
        }
        
    } // end class

}
