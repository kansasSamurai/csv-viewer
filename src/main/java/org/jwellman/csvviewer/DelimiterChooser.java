package org.jwellman.csvviewer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.jwellman.csvviewer.interfaces.TextChooserAware;
import org.jwellman.foundation.swing.XToggleButton;
import org.jwellman.swing.component.HorizontalGraphitePanel;

public class DelimiterChooser extends JPanel implements TextChooserAware {

	private static final long serialVersionUID = 1L;
	
	private String chosenText;

	public DelimiterChooser() {
        this.setLayout(new SpringLayout());
        this.setBorder(null);

        final Dimension d = new Dimension(22, 1);
        final ButtonGroup bg = new ButtonGroup(); 
        this.add(HorizontalGraphitePanel.createDefault(Arrays.asList(
                 HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector(",")).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("~")).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("|")).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("\\t")).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector(">")).addTo(bg).get(), null, d)
            )));
	}
	
	@Override
	public String getText() {
		return this.chosenText;
	}

    @SuppressWarnings("serial")
    private class DelimiterSelector extends AbstractAction {
        
        public DelimiterSelector(String s) {
            super(s);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof AbstractButton) {
                AbstractButton b = (AbstractButton)e.getSource();
                final String command = b.getActionCommand().toLowerCase();
                if (command.startsWith("choose")) {
                } else {
                }                                
            }
        }
        
    } // end class

}
