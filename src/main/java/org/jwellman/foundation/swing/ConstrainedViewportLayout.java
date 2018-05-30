package org.jwellman.foundation.swing;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.ViewportLayout;

/**
 * Derived from http://stackoverflow.com/questions/11587292/jscrollpane-not-wide-enough-when-vertical-scrollbar-appears
 * Usage: scrollPane.getViewport().setLayout(new ConstrainedViewPortLayout());
 *
 * @author Rick
 */
public class ConstrainedViewportLayout extends ViewportLayout {

    @Override
    public Dimension preferredLayoutSize(Container parent) {

        Dimension preferredViewSize = super.preferredLayoutSize(parent);

        Container viewportContainer = parent.getParent();
        if (viewportContainer != null) {
            Dimension parentSize = viewportContainer.getSize();
            preferredViewSize.height = parentSize.height;
        }

        return preferredViewSize;
    }
}