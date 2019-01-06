package org.jwellman.swing.uimgmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public class PaletteManager extends JPanel implements ActionListener {

	// i.e. "red":=0xFF0000, "green":=0x00FF00, "blue":=0x0000FF, "skyblue"=0xNNNNNN
	private Map<String,Color> paletteMap = new java.util.HashMap<>(); 

	private static PaletteManager singleton = new PaletteManager();
	
	public static PaletteManager get() {
		return singleton;
	}
	
	// may not need this unless I add it to the main gui
	private JColorChooser chooser = new JColorChooser();
	
	private JPanel pnlAllColor;

	private JButton btnNewColor;

	private static final String CMD_NEW = "New Color";
	
	private PaletteManager() {
		this.setLayout(new BorderLayout());
		
		this.btnNewColor = new JButton(CMD_NEW);
		this.btnNewColor.addActionListener(this);
		
		JPanel p = null; // reusable
		String[] collection; // reusable
		
		p = this.pnlAllColor = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p = new JPanel();
		p.add(this.btnNewColor);
		this.add(p, BorderLayout.SOUTH);		
		
		String palette = "shadowblue:0F263D midnightblue:173D7B bahamablue:28B3D9 aquablue:4ACEE4 silvergrey:D6DEE8";
		collection = palette.split(" ");
		for (String property : collection) {
			String[] pair = property.split(":");
			
			String strColor = pair[1];
			if (strColor.startsWith("0x")) ; else strColor = "0x" + strColor;
			Color color = Color.decode(strColor);
			
			this.paletteMap.put(pair[0], color);
			
			this.pnlAllColor.add(p(color, pair[0]));
		}
		
		this.add(this.pnlAllColor , BorderLayout.EAST);
		this.add(this.chooser, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case CMD_NEW :
			final Color newColor = chooser.getColor(); 
//			JColorChooser.showDialog(
//                    this, "New Palette Color", Color.black );
			
			final Component[] content = pnlAllColor.getComponents();
				pnlAllColor.removeAll();			
			for (Component c : content) {
				pnlAllColor.add(c);
			}	
			pnlAllColor.add(p(newColor, "new"));
			pnlAllColor.add(Box.createVerticalGlue());			
			pnlAllColor.revalidate(); pnlAllColor.repaint();
			
			break;
		}
		
	}
	
	/** 
	 * Factory/convenience method for creating palette panels.
	 * @return
	 */
	private JPanel p(Color c, String name) {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(c);
		
		panel.add(new JLabel(name), BorderLayout.CENTER); // temp
		
		return panel;
	}
		
}
