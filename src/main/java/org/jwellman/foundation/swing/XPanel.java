package org.jwellman.foundation.swing;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author rwellman
 */
public class XPanel extends javax.swing.JPanel {

    private JPanel child;

    private IWindow parent;

    public XPanel(JPanel p) {
        this.setLayout(new BorderLayout());
        this.child = p;
        this.add(p, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean aflag) {
        this.parent.setVisible(aflag);
    }

    public void setParent(IWindow i) {
        this.parent = i;
    }

    public JPanel getChild() {
        return this.child;
    }

}
