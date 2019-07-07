package org.jwellman.foarchive.extend;

import org.jwellman.foundation.swing.IWindow;
import org.jwellman.foundation.swing.XPanel;

/**
 * A base class providing common variables needed for basic Foundation app.
 * 
 * Refactor Notes:  Do not use/modify this old class... it was moved into
 * Foundation as .../extend/AbstractSimpleMain.java
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
    protected XPanel mainui;

}
