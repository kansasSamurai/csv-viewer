package org.jwellman.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Utilities {

    /**
     * Often we want components to span the entire width of the container;
     * this mimics behavior when laying out HTML documents and is usually
     * more natural and desired than the way most Swing components are 
     * natively sized.
     * 
     * A canonical example of this is putting a JLabel in a vertical BoxLayout.
     * 
     * @param c
     * @return
     */
    public static JComponent allowMaxWidth(JComponent c) {
        Dimension d = c.getPreferredSize(); 
        d.width = Short.MAX_VALUE;
        c.setMaximumSize(d);
        return c;
    }

    /**
     * Same as allowMaxWidth(JComponent c) except that sometimes we need to know the container
     * so that we can use its layout setting to get proper behavior. 
     * 
     * @param titlebar
     * @param north
     * @return
     */
    public static Component allowMaxWidth(JComponent c, JPanel p) {
        final LayoutManager layout = p.getLayout();
        
        if (layout instanceof BoxLayout) {
            c.setAlignmentX(0.5f);
        }

        return allowMaxWidth(c);
    }
    
}
