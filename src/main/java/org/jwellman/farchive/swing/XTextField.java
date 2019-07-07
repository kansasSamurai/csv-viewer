package org.jwellman.farchive.swing;

import javax.swing.JTextField;

/**
 * 
 * @author rwellman
 *
 */
public class XTextField extends XComponent {

    public static XTextField create() {
        return new XTextField();
    }
    
    private XTextField() {
        super(new JTextField());
    }
    
}
