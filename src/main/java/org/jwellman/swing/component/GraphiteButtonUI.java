package org.jwellman.swing.component;

import java.awt.*;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * A key to this UI design is that this is a non-opaque design
 * so the underlying panel will show through especially when not selected or pressed.
 *
 * Based on:
 * https://explodingpixels.wordpress.com/2009/12/02/creating-the-itunes-navigation-header-button/
 *
 * @author Rick
 */
public class GraphiteButtonUI extends BasicButtonUI {

    /** If true, will draw disabled state.  Otherwise, disabled is not drawn distinctly. */
    private boolean drawDisabledState = false;

    private Toolkit tk = Toolkit.getDefaultToolkit();
    
    @SuppressWarnings("rawtypes")
    private Map map = (Map) (tk.getDesktopProperty("awt.font.desktophints"));
    
    private static boolean useDrawString = false;

    private static Color TEXT_COLOR = new Color(0xcdcdcd); // Color.WHITE
    private static Color TEXT_PRESSED_COLOR = Color.WHITE;
    private static Color TEXT_SHADOW_COLOR = Color.BLACK; // Color.BLACK
    private static Color TEXT_DISABLED_COLOR = new Color(0x5d5d5d);

    // the gradient colors for when the button is selected.
    private static Color SELECTED_BACKGROUND_COLOR_1 = new Color(0x141414); // top.top 0x141414 | 0xffffff
    private static Color SELECTED_BACKGROUND_COLOR_2 = new Color(0x1e1e1e); // top.bottom 0x1e1e1e | 0x000000
    private static Color SELECTED_BACKGROUND_COLOR_3 = new Color(0x191919); // bottom.top 0x191919
    private static Color SELECTED_BACKGROUND_COLOR_4 = new Color(0x1e1e1e); // bottom.bottom 0x1e1e1e

    // the border colors for the button.
    private static Color SELECTED_TOP_BORDER = new Color(0x030303);
    private static Color SELECTED_BOTTOM_BORDER = new Color(0x292929);

    // the border colors between buttons.
    private static Color LEFT_BORDER = new Color(255,255,255,21);
    private static Color RIGHT_BORDER = new Color(0,0,0,125);

    private static final Color SELECTED_INNER_SHADOW_COLOR_1 = new Color(0x161616);
    private static final Color SELECTED_INNER_SHADOW_COLOR_2 = new Color(0x171717);
    private static final Color SELECTED_INNER_SHADOW_COLOR_3 = new Color(0x191919);

    @Override
    protected void installDefaults(AbstractButton button) {
        super.installDefaults(button);
        button.setBackground(new Color(0,0,0,0));
        button.setOpaque(false);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        // if the button is selected, paint the special background now.
        // if it is not selected paint the left and right highlight border.
        AbstractButton button = (AbstractButton) c;
        if (button.isSelected()) {
            paintButtonSelected(g, button);
        } else {
            // paint the border and border highlight if the button isn't selected.
            g.setColor(LEFT_BORDER);
            g.drawLine(0, 1, 0, button.getHeight()-2);
            g.setColor(RIGHT_BORDER);
            g.drawLine(button.getWidth()-1, 1, button.getWidth()-1, button.getHeight()-2);
        }

        super.paint(g, c);
    }

    /**
     * We need to override the paintText method so that we can paint
     * the text shadow. the paintText method in BasicButtonUI pulls
     * the color to use from the foreground property -- there is no
     * way to change this during the painting process without causing
     * an infinite sequence of events, so we must implement our own
     * text painting.
     *
     * @param gg
     * @param button
     * @param textRect
     * @param text
     */
    @Override
    protected void paintText(Graphics gg, AbstractButton button, Rectangle textRect, String text) {

        final Graphics2D g = (Graphics2D)gg;
        
        if (map != null) {
            g.addRenderingHints(map);
        } else {
            g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT); // VALUE_TEXT_ANTIALIAS_ON | VALUE_TEXT_ANTIALIAS_GASP            
        }
        
        final FontMetrics fontMetrics = g.getFontMetrics(button.getFont());
        final int mnemonicIndex = button.getDisplayedMnemonicIndex();

        // paint the shadow text.
        if (!button.getModel().isPressed()) {
            if (!button.getModel().isEnabled() && this.isDrawDisabledState()) {
                // do not draw shadow
            } else {
                g.setColor(TEXT_SHADOW_COLOR);
                if (useDrawString) {
                    g.drawString(
                            text,
                            textRect.x + getTextShiftOffset() + 1,
                            textRect.y + fontMetrics.getAscent() + getTextShiftOffset() + 1);   
                } else {
                    // TODO if windows 7, do not paint the shadow
                    BasicGraphicsUtils.drawStringUnderlineCharAt(
                            g, text, mnemonicIndex,
                            textRect.x + getTextShiftOffset() + 1,
                            textRect.y + fontMetrics.getAscent() + getTextShiftOffset() + 1);                    
                }
            }
        }

        // paint the actual text.
        Color c = TEXT_COLOR;
        if (!button.getModel().isEnabled()) {
            if (this.isDrawDisabledState()) c = TEXT_DISABLED_COLOR;
        } else {
            c = button.getModel().isPressed() ? TEXT_PRESSED_COLOR : TEXT_COLOR;
        }
        g.setColor(c);
        if (useDrawString) {
            g.drawString(
                    text, 
                    textRect.x + getTextShiftOffset(),
                    textRect.y + fontMetrics.getAscent() + getTextShiftOffset());
        } else {
            BasicGraphicsUtils.drawStringUnderlineCharAt(
                    g, text, mnemonicIndex,
                    textRect.x + getTextShiftOffset(),
                    textRect.y + fontMetrics.getAscent() + getTextShiftOffset());
        }
    }

    /**
     * Paints the selected buttons state, also used as the pressed state.
     */
    private void paintButtonSelected(Graphics graphics, AbstractButton button) {
        // calculate the middle of the area to paint.
        int midY = button.getHeight()/2;

        // paint the top half of the background with the corresponding gradient.
        Paint topPaint = new GradientPaint(0, 0, SELECTED_BACKGROUND_COLOR_1, 0, midY, SELECTED_BACKGROUND_COLOR_2);
        ((Graphics2D) graphics).setPaint(topPaint);
        graphics.fillRect(0, 0, button.getWidth(), midY);

        // paint the bottom half of the background with the corresponding gradient.
        Paint bottomPaint = new GradientPaint(0, midY + 1, SELECTED_BACKGROUND_COLOR_3, 0, button.getHeight(), SELECTED_BACKGROUND_COLOR_4);
        ((Graphics2D) graphics).setPaint(bottomPaint);
        graphics.fillRect(0, midY, button.getWidth(), button.getHeight());

        // draw the top and bottom border.
        graphics.setColor(SELECTED_TOP_BORDER);
        graphics.drawLine(0, 0, button.getWidth(), 0);
        graphics.setColor(SELECTED_BOTTOM_BORDER);
        graphics.drawLine(0, button.getHeight() - 1, button.getWidth(), button.getHeight() - 1);

        // paint the outer part of the inner shadow.
        graphics.setColor(SELECTED_INNER_SHADOW_COLOR_1);
        graphics.drawLine(0, 1, 0, button.getHeight()-2);
        graphics.drawLine(0, 1, button.getWidth(), 1);
        graphics.drawLine(button.getWidth()-1, 1, button.getWidth()-1, button.getHeight()-2);

        // paint the middle part of the inner shadow.
        graphics.setColor(SELECTED_INNER_SHADOW_COLOR_2);
        graphics.drawLine(1, 1, 1, button.getHeight()-2);
        graphics.drawLine(0, 2, button.getWidth(), 2);
        graphics.drawLine(button.getWidth()-2, 1, button.getWidth()-2, button.getHeight()-2);

        // paint the inner part of the inner shadow.
        graphics.setColor(SELECTED_INNER_SHADOW_COLOR_3);
        graphics.drawLine(2, 1, 2, button.getHeight()-2);
        graphics.drawLine(0, 3, button.getWidth(), 3);
        graphics.drawLine(button.getWidth()-3, 1, button.getWidth()-3, button.getHeight()-2);

    }

    @Override
    protected void paintButtonPressed(Graphics graphics, AbstractButton button) {
        paintButtonSelected(graphics, button);
    }

    public boolean isDrawDisabledState() {
        return drawDisabledState;
    }

    public void setDrawDisabledState(boolean drawDisabledState) {
        this.drawDisabledState = drawDisabledState;
    }

}
