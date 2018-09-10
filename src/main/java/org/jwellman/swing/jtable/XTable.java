package org.jwellman.swing.jtable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class XTable extends JTable {

	private static final long serialVersionUID = 1L;

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);

		int feature = 1;
		switch (feature) {
		case 1: //  Alternate row color
			if (isRowSelected(row))
				c.setBackground(this.getSelectionBackground());
			else
				c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);
//				if ((row % 2) == 0)
//					c.setBackground(Color.LIGHT_GRAY);
			break;
		default:
			;		
		}

		return c;
	}

}
