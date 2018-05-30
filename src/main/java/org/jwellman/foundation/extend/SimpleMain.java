package org.jwellman.foundation.extend;

import javax.swing.JPanel;

import org.jwellman.foundation.swing.IWindow;

/**
 * A base class providing common variables needed for basic Foundation app.
 * 
 * @author Rick
 *
 */
abstract public class SimpleMain {

    /**
     * A reference to your JPanel's container - for customization
     */
    protected IWindow window;

    /**
     * Your user interface - JPanel(s) only!
     */
    protected JPanel mainui;


}
