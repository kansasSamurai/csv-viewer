package org.jwellman.swing.jpanel;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * A JPanel extension that simply prevents most* parent layout managers
 * from manipulating its height by returning its *preferred* height when
 * its maximum size is requested.
 * 
 * This is most often used when using JPanels to "stack" components
 * vertically within a vertical BoxLayout which often results in
 * "extra" space at the bottom of the container.  Without this class,
 * the layout manager will assign "extra" space in the enclosing JPanel
 * and allocate it "within" each nested JPanel which does not give 
 * the desired effect of components "neatly" stacked in a vertical orientation. 
 * 
 * @author rwellman
 *
 */
public class RestrictedHeightPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public RestrictedHeightPanel(LayoutManager mgr) {
		super(mgr);
	}

	@Override
	public Dimension getMaximumSize() {
		final Dimension d = super.getMaximumSize();
		d.setSize(d.getWidth(), this.getPreferredSize().height);
		return d;
	}

}
