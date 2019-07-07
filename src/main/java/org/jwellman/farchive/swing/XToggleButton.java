package org.jwellman.foundation.swing;

import javax.swing.JToggleButton;

/**
 * 
 * @author rwellman
 *
 */
public class XToggleButton extends XComponent {

    public static XToggleButton create() {
        return new XToggleButton();
    }
    
    private XToggleButton() {
        super(new JToggleButton());
    }
}
