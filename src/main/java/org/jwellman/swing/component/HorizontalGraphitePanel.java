package org.jwellman.swing.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

/**
 * Based on:
 * https://explodingpixels.wordpress.com/2009/11/13/creating-the-itunes-navigation-header/
 *
 * @author Rick
 */
public class HorizontalGraphitePanel extends JComponent {

	private static final long serialVersionUID = 1L;
	
	private static final Font FONT_DEFAULT = new Font("Verdana", Font.PLAIN, 14); // new Font("Segoe UI", Font.PLAIN, 14);

	/**
	 * The GraphiteButtonUI class is thread-safe and can be reused by
	 * all button instances so this static instance is sufficient for
	 * any application.
	 */
	public static final GraphiteButtonUI BUTTONUI = new GraphiteButtonUI();
	
	// A default dimension to use for buttons added to this panel; change with accessor
	private static Dimension BUTTON_DIMENSION = new Dimension(80, 1);
	
    public static void setButtonDimension(Dimension d) {
		BUTTON_DIMENSION = d;
	}

	// the hard-coded preferred height. ideally this would be derived from the font size.
    private static int HEADER_HEIGHT = 25; // 25 is the original value

    // http://www.colorzilla.com/colors/393939+2e2e2e+232323+282828+171717+292929+353535+383838+2c2c2c+363636
    
    // the background colors used in the multi-stop gradient.
    private static Color BACKGROUND_COLOR_1 = new Color(0x393939);
    private static Color BACKGROUND_COLOR_2 = new Color(0x2e2e2e);
    private static Color BACKGROUND_COLOR_3 = new Color(0x232323);
    private static Color BACKGROUND_COLOR_4 = new Color(0x282828);

    // the color to use for the top and bottom border.
    private static Color BORDER_COLOR = new Color(0x171717);

    // the inner shadow colors on the top of the header.
    private static Color TOP_SHADOW_COLOR_1 = new Color(0x292929);
    private static Color TOP_SHADOW_COLOR_2 = new Color(0x353535);
    private static Color TOP_SHADOW_COLOR_3 = new Color(0x383838);

    // the inner shadow colors on the bottom of the header.
    private static Color BOTTOM_SHADOW_COLOR_1 = new Color(0x2c2c2c);
    private static Color BOTTOM_SHADOW_COLOR_2 = new Color(0x363636);

    @Override
    public Dimension getPreferredSize() {
        final Dimension d = super.getPreferredSize(); //new Dimension(-1, HEADER_HEIGHT);
        d.height = HEADER_HEIGHT; // d.setSize(d.getWidth(), HEADER_HEIGHT);
        return d;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();

        // calculate the middle of the area to paint.
        int midY = getHeight()/2;

        // paint the top half of the background with the corresponding
        // gradient. note that if we were using Java 6, we could use a
        // LinearGradientPaint with multiple stops.
        Paint topPaint = new GradientPaint(0, 0, BACKGROUND_COLOR_1, 0, midY, BACKGROUND_COLOR_2);
        graphics.setPaint(topPaint);
        graphics.fillRect(0, 0, getWidth(), midY);

        // paint the top half of the background with the corresponding gradient.
        Paint bottomPaint = new GradientPaint(0, midY + 1, BACKGROUND_COLOR_3, 0, getHeight(), BACKGROUND_COLOR_4);
        graphics.setPaint(bottomPaint);
        graphics.fillRect(0, midY, getWidth(), getHeight());

        // draw the top inner shadow.
        graphics.setColor(TOP_SHADOW_COLOR_1);
        graphics.drawLine(0, 1, getWidth(), 1);
        graphics.setColor(TOP_SHADOW_COLOR_2);
        graphics.drawLine(0, 2, getWidth(), 2);
        graphics.setColor(TOP_SHADOW_COLOR_3);
        graphics.drawLine(0, 3, getWidth(), 3);

        // draw the bottom inner shadow.
        graphics.setColor(BOTTOM_SHADOW_COLOR_1);
        graphics.drawLine(0, getHeight() - 3, getWidth(), getHeight() - 3);
        graphics.setColor(BOTTOM_SHADOW_COLOR_2);
        graphics.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);

        // draw the top and bottom border.
        graphics.setColor(BORDER_COLOR);
        graphics.drawLine(0, 0, getWidth(), 0);
        graphics.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

        graphics.dispose();
    }

    /**
     * Factory method to a HorizontalGraphitePanel in its most common usage;
     * namely with a GridLayout.
     * @param <T>
     * 
     * @param listOfButtons
     * @param font
     * @param size
     * @return a new HorizontalGraphitePanel component
     */
    public static <T extends AbstractButton> HorizontalGraphitePanel createDefault(List<T> listOfButtons) {
    	
    	final HorizontalGraphitePanel panel = new HorizontalGraphitePanel();
    	panel.setLayout(new java.awt.GridLayout(1,0));
    	    	
    	if (listOfButtons != null) {
	    	for (AbstractButton button : listOfButtons) {
	    		panel.add(button);
	    	}
    	}
    	
    	return panel;
    }

    /**
     * Factory method to properly create a JButton with the GraphiteButtonUI installed.
     * 
     * @param label
     * @param font
     * @param size
     * @return
     */
    public static JButton createButton(String label, Font font, Dimension size) {
        final JButton b = new JButton(label);
        return (JButton) decorateButton(b, font, size);
    }

    /**
     * Factory method to properly create a JToggleButton with the GraphiteButtonUI installed.
     * 
     * @param label if null, set to empty string (since clients often control label with an Action object)
     * @param font
     * @param size
     * @return
     */
    public static JToggleButton createToggleButton(String label, Font font, Dimension size) {
        final JToggleButton b = new JToggleButton((label == null) ? "" : label);
        return (JToggleButton) decorateButton(b, font, size);
    }

    /**
     * Convenience method to decorate a user-defined Button object.
     * <p>
     * Note that this is used internally by createButton().
     * 
     * @param b
     * @param font
     * @param size
     * @return
     */
    public static <T extends AbstractButton> T decorateButton(T b, Font font, Dimension size) {
        b.setUI(BUTTONUI); // this *must* be first
        b.setFont((font != null) ? font : FONT_DEFAULT );
        b.setMinimumSize((size != null) ? size : (size = BUTTON_DIMENSION));
        b.setPreferredSize(size);
        b.setMaximumSize(size);
        b.setBorder(null); // Some L&F (i.e. Metal) apply borders to buttons by default; Nimbus does not
        return b;
    }

}
