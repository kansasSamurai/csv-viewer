package org.jwellman.swing.uimgmt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jwellman.swing.icon.ColorIcon;
import org.jwellman.swing.jpanel.RestrictedHeightPanel;

/**
 * 
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public class PaletteManager extends JPanel implements ActionListener {

	private static PaletteManager singleton = new PaletteManager();
	
	public static PaletteManager get() {
		return singleton;
	}
	
	private JPanel pnlPalette;

	private JButton btnNewColor;
	
	private JTextField txtNewColor;

    // may not need this unless I add it to the main gui
    private JColorChooser chooser = new JColorChooser();
    
    // i.e. "red":=0xFF0000, "green":=0x00FF00, "blue":=0x0000FF, "skyblue"=0xNNNNNN
    private Map<String,Color> paletteMap = new java.util.HashMap<>(); 

	private static final String CMD_NEW = "Add to Palette";
	
	private PaletteManager() {
		this.setLayout(new BorderLayout());
		
		this.txtNewColor = new JTextField("NameTheColor");
		
		this.btnNewColor = new JButton(CMD_NEW);
		this.btnNewColor.addActionListener(this);
		
		JPanel p = null; // reusable
		String[] collection; // reusable
		
		p = this.pnlPalette = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(BorderFactory.createTitledBorder("Palette"));
		
		p = new JPanel();
		p.add(this.txtNewColor);
		p.add(this.btnNewColor);		
		this.add(p, BorderLayout.SOUTH);		
		
		String palette = "shadowblue:0F263D midnightblue:173D7B bahamablue:28B3D9 aquablue:4ACEE4 silvergrey:D6DEE8";
		collection = palette.split(" ");
		for (String property : collection) {
			String[] pair = property.split(":");
			
			String strColor = pair[1];
			if (strColor.startsWith("0x")) ; else strColor = "0x" + strColor;
			
			final Color color = Color.decode(strColor);			
			this.paletteMap.put(pair[0], color);
			
			this.pnlPalette.add(p(color, pair[0]));
		}
		
		this.add(this.pnlPalette , BorderLayout.EAST);
		this.add(this.chooser, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case CMD_NEW :
			final Color newColor = chooser.getColor(); 
            this.paletteMap.put(this.txtNewColor.getText(), newColor);

			pnlPalette.add(p(newColor, this.txtNewColor.getText()));
            pnlPalette.revalidate(); // pnlPalette.repaint();

			break;
		}
		
	}
	
	/** 
	 * Factory/convenience method for creating palette panels.
	 * @return
	 */
	private JPanel p(Color c, String name) {
		final JPanel panel = new RestrictedHeightPanel (new BorderLayout());
		
        panel.add(new JLabel(name), BorderLayout.WEST); // temp

        int version = 2;
        switch (version) {
        case 1: {
            JPanel color = new JPanel(); 
            color.setLayout(new BoxLayout(color, BoxLayout.LINE_AXIS));
            color.setBackground(c);
            color.add(Box.createHorizontalStrut(20));
            panel.add(color, BorderLayout.EAST);
        }
            break;
        case 2: {
            Icon swatch = new ColorIcon(c, 13);

            JPanel color = new JPanel(); // default is FlowLayout 
            color.add(new JLabel(swatch));
            panel.add(color, BorderLayout.EAST);
        }
            break;
        }
		
		return panel;
	}
		
}
