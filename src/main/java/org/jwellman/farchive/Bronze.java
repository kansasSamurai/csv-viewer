package org.jwellman.farchive;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.jwellman.foundation.swing.XInternalFrame;
import org.jwellman.foundation.swing.XPanel;

/**
 *
 * @author Rick
 */
public class Bronze extends Stone {

    private final Map<String,XPanel> panels = new HashMap<>();

    private final Map<String,XInternalFrame> iframes = new HashMap<>();

    public XPanel registerUI(String name, JPanel ui) {
        final XPanel p = new XPanel(ui);
        p.setName(name);
        panels.put(name, p);
        return p;
    }

    @Override
    protected void initializeOtherWindows() {
        if (this.isDesktop) {
            for (String name : panels.keySet()) {
                final XPanel p = panels.get(name);
                if (p == this.panel) {
                    /* Do nothing, it already has an iframe */
                } else {
                    final XInternalFrame f = new XInternalFrame();
                    f.add(p); f.pack();
                    p.setParent(f); iframes.put(p.getName(), f);
                    this.getDesktop().add(f);
                }
            }
        }
    }

}
