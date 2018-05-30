package org.jwellman.foundation.swing;

/**
 *
 * @author Rick
 */
public class XInternalFrame extends javax.swing.JInternalFrame implements IWindow {

    public XInternalFrame() {
        super();
    }

    public XInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

}
