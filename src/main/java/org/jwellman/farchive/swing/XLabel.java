package org.jwellman.farchive.swing;

import javax.swing.JLabel;

/**
 * 
 * @author rwellman
 *
 */
public class XLabel extends XComponent {

    public static XLabel create() {
        return new XLabel();
    }
    
    private XLabel() {
        super(new JLabel());
    }

}
