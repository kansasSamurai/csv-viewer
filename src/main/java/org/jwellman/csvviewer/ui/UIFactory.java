package org.jwellman.csvviewer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class UIFactory {

	public static class BORDERS {
		public static final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		public static final Border BORDER_RIGHT_2PX = BorderFactory.createEmptyBorder(0, 0, 0, 2);
		public static final Border BORDER_MATTE = BorderFactory.createMatteBorder(5, 1, 1, 1, new Color(249, 249, 249));
		public static final Border BORDER_ETCHED = BorderFactory.createEtchedBorder();
		public static final Border BORDER_LINE = BorderFactory.createLineBorder(Color.black, 1);
	}

	public static class FONTS {
		public static final Font FONT_SEGOE_UI = new Font("Segoe UI", Font.PLAIN, 12);
		public static final Font FONT_SEGOE_UI_BOLD = new Font("Segoe UI", Font.BOLD, 14);
		// I like Calibri at font sizes above 12pt; its numbers are 'monospaced' which is highly desirable to me
		public static final Font FONT_CALIBRI = new Font("Calibri", Font.PLAIN, 14);
		public static final Font FONT_CALIBRI_BOLD = new Font("Calibri", Font.BOLD, 12);
		// I like Lucida up to 12pt; above that it tends to look a little "stretched" (but is still monospaced correctly)
		public static final Font FONT_LUCIDA = new Font("Lucida Console", Font.PLAIN, 12);
		public static final Font FONT_VERDANA = new Font("Verdana", Font.PLAIN, 14);
	}

	public static class PALETTE {
		public static final Color COLOR_GREY_MED = new Color(136, 136, 136);
		public static final Color COLOR_GREY_DARKEST = new Color(64, 64, 64);
		public static final Color COLOR_EAST_TEXT = new Color(0xcdcdcd);
	}

	public static final JLabel createDarkLabel(String text) {
		final JLabel label = new JLabel(text);
		label.setFont(FONTS.FONT_SEGOE_UI);
		label.setOpaque(true);
		label.setBackground(PALETTE.COLOR_GREY_DARKEST);
		label.setForeground(PALETTE.COLOR_EAST_TEXT);

		return label;
	}

	public static final JLabel createLightLabel(String text) {
		final JLabel label = new JLabel(text);
		label.setFont(FONTS.FONT_SEGOE_UI);
		label.setOpaque(true);
		label.setForeground(PALETTE.COLOR_GREY_DARKEST);
		label.setBackground(PALETTE.COLOR_EAST_TEXT);

		return label;
	}

	public static final JLabel createFooterDataLabel(String text) {
		final JLabel label = createLightLabel(text);
		label.setBorder(BORDERS.BORDER_RIGHT_2PX);

		return label;
	}

	public static JPanel createDarkLabelBox() {
		final JPanel panel = new JPanel(new BorderLayout());
		// new JPanel(new BorderLayout()); // new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
		panel.setBorder(BORDERS.BORDER_LINE);
		panel.setBackground(PALETTE.COLOR_EAST_TEXT);

		return panel;
	}

}
