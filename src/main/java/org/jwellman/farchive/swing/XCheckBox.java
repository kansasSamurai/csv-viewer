package org.jwellman.foundation.swing;

import javax.swing.JCheckBox;

/**
 * 
 * @author rwellman
 *
 */
public class XCheckBox extends XComponent {

    public static XCheckBox create() {
        return new XCheckBox();
    }
    
    private XCheckBox() {
        super(new JCheckBox());
    }
    
}
