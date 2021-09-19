package org.jwellman.swing.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D ;
import java.awt.RenderingHints ;

import javax.swing.Icon;

public class CloseIcon implements Icon {

    public void paintIcon(Component c, Graphics g, int x, int y) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.RED);
        g2.drawRect(0, 0, this.getIconWidth()-1, this.getIconHeight()-1);

        final int top = 5, bottom = this.getIconHeight() - 6;
        final int left = 5, right = this.getIconWidth() - 6;
        g2.drawLine(left, top, right, bottom);
        g2.drawLine(right, top, left, bottom);
        
        g2.dispose();
    }

    public int getIconWidth() {
        return 15; 
    }

    public int getIconHeight() {
        return 15;
    }
    
}
