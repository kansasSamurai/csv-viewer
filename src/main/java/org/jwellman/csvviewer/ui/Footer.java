package org.jwellman.csvviewer.ui;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Footer extends JPanel {

	final JPanel content = new JPanel();
	final JLabel labeltotal = UIFactory.createDarkLabel(" Total Data Rows  ");
	final JLabel total = UIFactory.createFooterDataLabel("???");
	final JLabel labelsearch = UIFactory.createDarkLabel(" Search Results  ");
	final JLabel search = UIFactory.createFooterDataLabel("?");
	final JLabel labelselected = UIFactory.createDarkLabel(" Rows Selected  ");
	final JLabel selected = UIFactory.createFooterDataLabel("0");
	
	final JPanel totalbox = UIFactory.createDarkLabelBox();
	final JPanel searchbox = UIFactory.createDarkLabelBox();
	final JPanel selectedbox = UIFactory.createDarkLabelBox();

	// totalrows does not change after construction
	int totalrows = 0;

	// displayedrows is only less than total after search
	int displayedrows = 0;

	public Footer(int rows) {
		this.setLayout(new BorderLayout());

		this.totalrows = this.displayedrows = rows;
		total.setText(Integer.toString(totalrows));
		search.setText(Integer.toString(displayedrows));

		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		this.add(content, BorderLayout.NORTH);

		totalbox.add(labeltotal, BorderLayout.WEST);
//		totalbox.add(Box.createHorizontalStrut(4));
//		totalbox.add(Box.createHorizontalGlue());
		totalbox.add(total, BorderLayout.EAST);
		content.add(totalbox);
		content.add(Box.createHorizontalStrut(2));

		searchbox.add(labelsearch, BorderLayout.WEST);
//		searchbox.add(Box.createHorizontalStrut(4));
//		searchbox.add(Box.createHorizontalGlue());
		searchbox.add(search, BorderLayout.EAST);
		content.add(searchbox);
		content.add(Box.createHorizontalStrut(2));

		selectedbox.add(labelselected, BorderLayout.WEST);
//		selectedbox.add(Box.createHorizontalStrut(4));
//		selectedbox.add(Box.createHorizontalGlue());
		selectedbox.add(selected, BorderLayout.EAST);
		content.add(selectedbox);

//		content.add(Box.createHorizontalGlue());
	}

	public void setDisplayedRows(int rowCount) {
		this.displayedrows = rowCount;
		search.setText(Integer.toString(displayedrows));
	}

}

