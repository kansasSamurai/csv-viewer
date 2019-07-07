package org.jwellman.farchive.swing;

import javax.swing.JButton;

/**
 * 
 * @author rwellman
 *
 */
public class XButton extends XComponent {

    public static XButton create() {
        return new XButton();
    }
    
    private XButton() {
        super(new JButton());
    }
    
}
