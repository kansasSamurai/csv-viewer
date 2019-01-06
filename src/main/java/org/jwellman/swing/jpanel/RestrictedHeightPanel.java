package org.jwellman.swing.jpanel;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * A JPanel extension that simply prevents most* parent layout managers
 * from manipulating its size by returning its preferred size when
 * its maximum size is requested.
 * 
 * @author Rick
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
