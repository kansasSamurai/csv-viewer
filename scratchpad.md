# scratchpad.md
As the filename implies, this is a scratchpad... its contents will vary over time
and are to be considered unimportant to the build/execution of this application.

## 8/27/2019

```
package org.rwellman.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.rwellman.demo.aterai.ListItem;
import org.rwellman.demo.aterai.ReorderableList;

/**
 * Modified version of:
 * https://java-swing-tips.blogspot.com/2008/10/rubber-band-selection-drag-and-drop.html
 * Also found at:
 * https://github.com/aterai/java-swing-tips
 * 
	The MIT License (MIT)
	
	Copyright (c) 2015 TERAI Atsuhiro
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
 * 
 * 
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public final class AteraiMainPanel extends JPanel {

	public static void main(String... args) {
		EventQueue.invokeLater(() -> createAndShowGui());
	}

	public static void createAndShowGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("List.dropLineColor", Color.orange);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
			Toolkit.getDefaultToolkit().beep();
		}
		
		final JFrame frame = new JFrame("@title@");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new AteraiMainPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private AteraiMainPanel() {

		super(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.blue));

		final DefaultListModel<ListItem> model = new DefaultListModel<>();
		// [XP Style Icons - Download](https://xp-style-icons.en.softonic.com/)
		model.addElement(new ListItem("asdasdfsd", "wi0009-32.png"));
		model.addElement(new ListItem("12345", "wi0054-32.png"));
		model.addElement(new ListItem("ADFFDF.asd", "wi0062-32.png"));
		model.addElement(new ListItem("test", "wi0063-32.png"));
		model.addElement(new ListItem("32.png", "wi0064-32.png"));
		model.addElement(new ListItem("asdfsd.jpg", "wi0096-32.png"));
		model.addElement(new ListItem("6896", "wi0111-32.png"));
		model.addElement(new ListItem("t467467est", "wi0122-32.png"));
		model.addElement(new ListItem("test123", "wi0124-32.png"));
		model.addElement(new ListItem("test(1)", "wi0126-32.png"));

		final ReorderableList<ListItem> list = new ReorderableList<ListItem>(model);
		this.add(new JScrollPane(list));
		this.setPreferredSize(new Dimension(320, 240));
	}

}

// class DotBorder extends EmptyBorder {
//   protected DotBorder(Insets borderInsets) {
//     super(borderInsets);
//   }
//   protected DotBorder(int top, int left, int bottom, int right) {
//     super(top, left, bottom, right);
//   }
//   @Override public boolean isBorderOpaque() {
//     return true;
//   }
//   @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
//     Graphics2D g2 = (Graphics2D) g.create();
//     g2.translate(x, y);
//     g2.setPaint(new Color(~SystemColor.activeCaption.getRGB()));
//     // new Color(200, 150, 150));
//     // g2.setStroke(dashed);
//     // g2.drawRect(0, 0, w - 1, h - 1);
//     BasicGraphicsUtils.drawDashedRect(g2, 0, 0, w, h);
//     g2.dispose();
//   }
//   // @Override public Insets getBorderInsets(Component c)
//   // @Override public Insets getBorderInsets(Component c, Insets insets)
// }

package org.rwellman.demo.aterai;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class ReorderableList<E extends ListItem> extends JList<E> {

	private Color rubberBandColor;
	// protected final Path2D rubberBand = new Path2D.Double();
	private static final AlphaComposite ALPHA = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f);

	private transient RubberBandingListener rbl;

	public ReorderableList(ListModel<E> model) {
		super(model);
	}

	@Override
	public void updateUI() {
//    setSelectionForeground(null); // Nimbus
//    setSelectionBackground(null); // Nimbus
		setCellRenderer(null);
		setTransferHandler(null);
		removeMouseListener(rbl);
		removeMouseMotionListener(rbl);
		super.updateUI();

		rubberBandColor = makeRubberBandColor(getSelectionBackground());
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setFixedCellWidth(62);
		setFixedCellHeight(62);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		setCellRenderer(new ListItemListCellRenderer<>());
		rbl = new RubberBandingListener();
		addMouseMotionListener(rbl);
		addMouseListener(rbl);

		// putClientProperty("List.isFileList", Boolean.TRUE);
		
		// Customize drop line/color
		// To modify the color, use this method; however, it cannot be placed
		// in this class.  Rather, it must be called before the look and feel
		// has been set via UIManager.setLookAndFeel()
		// UIManager.put("List.dropLineColor", Color.orange);		
		
		getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setTransferHandler(new AteraiListItemTransferHandler());
		setDropMode(DropMode.INSERT);
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (getDragEnabled()) {
			return;
		}
		
		// If we are not in the middle of dragging an item,
		// then we must be rubberbanding, so draw the rubberband.
		final Graphics2D g2 = (Graphics2D) g.create(); {
			
			// draw the border
			g2.setPaint(getSelectionBackground());
			g2.draw(rbl.getRubberBand());
			
			// fill the border
			g2.setPaint(rubberBandColor);
			g2.setComposite(ALPHA);
			g2.fill(rbl.getRubberBand());
			
		} g2.dispose();
	}

	private static Color makeRubberBandColor(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return r > g ? r > b ? new Color(r, 0, 0) : new Color(0, 0, b)
				: g > b ? new Color(0, g, 0) : new Color(0, 0, b);
	}

}

package org.rwellman.demo.aterai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * 
 * @author rwellman
 *
 * @param <E> a class that extends ListItem
 */
public class ListItemListCellRenderer<E extends ListItem> implements ListCellRenderer<E> {

	// As a cell renderer, these are flyweight components:
	private final JPanel jpanel = new JPanel(new BorderLayout());
	private final JLabel icon = new JLabel((Icon) null, SwingConstants.CENTER);
	private final JLabel label = new JLabel("", SwingConstants.CENTER);

	private final Border focusBorder = UIManager.getBorder("List.focusCellHighlightBorder");
	private final Border noFocusBorder; // = UIManager.getBorder("List.noFocusBorder");
	private final Border gapBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private final Border itemBorder = BorderFactory.createEmptyBorder(2, 3, 2, 1);
	private Border cellBorder;

	protected ListItemListCellRenderer() {
		Border b = UIManager.getBorder("List.noFocusBorder");
		if (Objects.isNull(b)) { // Nimbus???
			Insets i = focusBorder.getBorderInsets(label);
			b = BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right);
		}
		noFocusBorder = b;
		
		icon.setOpaque(false);

		label.setForeground(jpanel.getForeground());
		label.setBackground(jpanel.getBackground());
		label.setBorder(noFocusBorder);
		
		cellBorder = gapBorder;
//		cellBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.blue), gapBorder);
//		cellBorder = BorderFactory.createCompoundBorder(itemBorder, cellBorder);
		jpanel.setOpaque(false);
		jpanel.setBorder(cellBorder); // (BorderFactory.createEmptyBorder(2, 2, 2, 2));
		jpanel.add(icon);
		jpanel.add(label, BorderLayout.SOUTH);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
		
		label.setText(value.title);
		label.setBorder(cellHasFocus ? focusBorder : noFocusBorder);
		
		if (isSelected) {
			icon.setIcon(value.sicon);
//			jpanel.setOpaque(true); // new
//			jpanel.setBackground(Color.black); // new
			
			label.setForeground(list.getSelectionForeground());
			label.setBackground(list.getSelectionBackground());
			label.setOpaque(true); // true
		} else {
			icon.setIcon(value.nicon);
//			jpanel.setOpaque(false); // new 
			
			label.setForeground(list.getForeground());
			label.setBackground(list.getBackground());
			label.setOpaque(false); // false
		}
		
		return jpanel;
	}
	
}

package org.rwellman.demo.aterai;

import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * 
 * @author rwellman
 *
 */
public class ListItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public final String title;
	public final ImageIcon nicon;
	public final ImageIcon sicon;
	private final ImageProducer ip;
	
	private static final SelectedImageFilter imageFilter = new SelectedImageFilter(32, 32);

	public ListItem(String title, String iconfile) {
		this.title = title;
		this.nicon = new ImageIcon(getClass().getResource(iconfile));

		ip = new FilteredImageSource(nicon.getImage().getSource(), imageFilter);
		this.sicon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(ip));
	}

}

package org.rwellman.demo.aterai;

import java.awt.image.RGBImageFilter;

/**
 * 
 * @author rwellman
 *
 */
public class SelectedImageFilter extends RGBImageFilter {

	private int width;
	private int height;
	private double edge;
	
	public SelectedImageFilter(int w, int h) {
		this.width = w;
		this.height = h;
		if (w != 0 && h != 0) {
			this.edge = Math.sqrt(width*width + height*height);;
		}
	}
	
	@Override
	public int filterRGB(int x, int y, int argb) {

		int red = argb & 0x00FF0000; red >>>= 16;
	    int green = argb & 0x0000FF00; green >>>= 8;
	    int blue = argb & 0x0000FF;

	    final int original = 1, redblue = 2, greyscale = 3, alpha = 4, shadow = 5;
		
		switch (original) {
		case original:
			return (argb & 0xFF_FF_FF_00) | ((argb & 0xFF) >> 1);
		case redblue:
			return (   (argb & 0xff00ff00)
	                | ((argb & 0xff0000) >> 16)
	                | ((argb & 0xff) << 16)
	        );
		case greyscale:
    	    int grey = (red + green + blue)/3;
    	    return (0x000000FF << 24) | (grey << 16) | (grey << 8) | grey;
	    case alpha: // THIS IS STILL UNDER CONSTRUCTION
	    	return (argb | 0xFF000000) & 0x77FFFFFF;
	    case shadow:
    	    double fraction = 1.0 - Math.sqrt(x*x + y*y)/edge;
    	    if (fraction <= 0) return 0xFF000000;
    	    
    	    int r = (int) (red * fraction);
    	    int g = (int) (green * fraction);
    	    int b = (int) (blue * fraction);
    	    return (0x000000FF << 24) | (r << 16) | (g << 8) | b; 
	    default:
	    	return 0x000000FF;
		}

	}
	
}

package org.rwellman.demo.aterai;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * Demo - BasicDnD (The Java™ Tutorials > Creating a GUI With JFC/Swing > Drag and Drop and Data Transfer)
 * https://docs.oracle.com/javase/tutorial/uiswing/dnd/basicdemo.html
 *  
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public class AteraiListItemTransferHandler extends TransferHandler {

	protected static final DataFlavor FLAVOR = new DataFlavor(List.class, "List of items");

	private int[] indices;
	private int addIndex = -1; 	// Location where items were added
	private int addCount; 		// Number of items added.

// protected AteraiListItemTransferHandler() {
//   super();
//   localObjectFlavor = new ActivationDataFlavor(
//       Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
// }

	@Override
	protected Transferable createTransferable(JComponent c) {
		
		c.getRootPane().getGlassPane().setVisible(true);
		
		final JList<?> source = (JList<?>) c;
		final List<?> transferedObjects = source.getSelectedValuesList();
				
		indices = source.getSelectedIndices();
		// return new DataHandler(transferedObjects, FLAVOR.getMimeType());
		
		return new Transferable() {
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { FLAVOR };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return Objects.equals(FLAVOR, flavor);
			}

			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor)) {
					return transferedObjects;
				} else {
					throw new UnsupportedFlavorException(flavor);
				}
			}
		}; // end Transferable
		
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		return info.isDrop() && info.isDataFlavorSupported(FLAVOR);
	}

	@Override
	public int getSourceActions(JComponent c) {
		System.out.println("getSourceActions");
		
		c.getRootPane().getGlassPane().setCursor(DragSource.DefaultMoveDrop);
		// glassPane.setVisible(true);
		
		return TransferHandler.MOVE; // TransferHandler.COPY_OR_MOVE;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean importData(TransferHandler.TransferSupport info) {
		TransferHandler.DropLocation tdl = info.getDropLocation();
		if (!canImport(info) || !(tdl instanceof JList.DropLocation)) {
			return false;
		}
		JList.DropLocation dl = (JList.DropLocation) tdl;
		JList<?> target = (JList<?>) info.getComponent();
		DefaultListModel<Object> listModel = (DefaultListModel<Object>) target.getModel();
		// boolean insert = dl.isInsert();
		int max = listModel.getSize();
		int index = dl.getIndex();
		// index = index < 0 ? max : index; // If it is out of range, it is appended to
		// the end
		// index = Math.min(index, max);
		index = index >= 0 && index < max ? index : max;
		addIndex = index;
		try {
			List<?> values = (List<?>) info.getTransferable().getTransferData(FLAVOR);
			for (Object o : values) {
				int i = index++;
				listModel.add(i, o);
				target.addSelectionInterval(i, i);
			}
			addCount = values.size();
			return true;
		} catch (UnsupportedFlavorException | IOException ex) {
			return false;
		}
	}

	@Override
	protected void exportDone(JComponent c, Transferable data, int action) {
		System.out.println("exportDone");
		
		c.getRootPane().getGlassPane().setVisible(false);
		// glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		cleanup(c, action == TransferHandler.MOVE);
	}

	private void cleanup(JComponent c, boolean remove) {
		if (remove && Objects.nonNull(indices)) {
			// If we are moving items around in the same list, we
			// need to adjust the indices accordingly, since those
			// after the insertion point have moved.
			if (addCount > 0) {
				for (int i = 0; i < indices.length; i++) {
					if (indices[i] >= addIndex) {
						indices[i] += addCount;
					}
				}
			}
			JList<?> source = (JList<?>) c;
			DefaultListModel<?> model = (DefaultListModel<?>) source.getModel();
			for (int i = indices.length - 1; i >= 0; i--) {
				model.remove(indices[i]);
			}
		}
		indices = null;
		addCount = 0;
		addIndex = -1;
	}

}

package org.rwellman.demo.aterai;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.stream.IntStream;

import javax.swing.JList;
import javax.swing.event.MouseInputAdapter;

/**
 * 
 * @author rwellman
 *
 */
public class RubberBandingListener extends MouseInputAdapter {
	
	private final Point srcPoint = new Point();

	private final Path2D rubberBand = new Path2D.Double();

	@Override
	public void mouseDragged(MouseEvent e) {
		final JList<?> l = (JList<?>) e.getComponent();
		if (l.getDragEnabled()) {
			return;
		}
		
		Point destPoint = e.getPoint();
		Path2D rb = this.getRubberBand();
		rb.reset();
		rb.moveTo(srcPoint.x, srcPoint.y);
		rb.lineTo(destPoint.x, srcPoint.y);
		rb.lineTo(destPoint.x, destPoint.y);
		rb.lineTo(srcPoint.x, destPoint.y);
		rb.closePath();

		// JDK 1.7.0: l.setSelectedIndices(getIntersectsIcons(l, rubberBand));
		int[] indices = IntStream.range( 0, l.getModel().getSize() )
				.filter(i -> rb.intersects(l.getCellBounds(i, i))).toArray();
		// System.out.println("idc: " + indices);
		l.setSelectedIndices(indices); // << this may prove problematic if you have a list selection listener as I think it will fire with every mousedrag event
		l.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		JList<?> l = (JList<?>) e.getComponent();
		l.setFocusable(true);
		// if (Objects.isNull(srcPoint) || !getDragEnabled()) {
		// Component glassPane = l.getRootPane().getGlassPane();
		// glassPane.setVisible(false);
		// }
		this.getRubberBand().reset();
		l.setDragEnabled(l.getSelectedIndices().length > 0);
		l.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		JList<?> l = (JList<?>) e.getComponent();
		int index = l.locationToIndex(e.getPoint());
		Rectangle rect = l.getCellBounds(index, index);
		if (rect.contains(e.getPoint())) {
			l.setFocusable(true);
			if (l.getDragEnabled()) {
				return;
			}
			// System.out.println("ccc:" + startSelectedIndex);
			l.setSelectedIndex(index);
		} else {
			l.clearSelection();
			l.getSelectionModel().setAnchorSelectionIndex(-1);
			l.getSelectionModel().setLeadSelectionIndex(-1);
			l.setFocusable(false);
			l.setDragEnabled(false);
		}
		srcPoint.setLocation(e.getPoint());
		l.repaint();
	}

	public Path2D getRubberBand() {
		return rubberBand;
	}
	
	// // JDK 1.7.0
	// private static int[] getIntersectsIcons(JList<?> l, Shape rect) {
	// ListModel model = l.getModel();
	// List<Integer> ll = new ArrayList<>(model.getSize());
	// for (int i = 0; i < model.getSize(); i++) {
	// if (rect.intersects(l.getCellBounds(i, i))) {
	// ll.add(i);
	// }
	// }
	// // JDK 1.8.0: return ll.stream().mapToInt(Integer::intValue).toArray();
	// int[] il = new int[ll.size()];
	// for (int i = 0; i < ll.size(); i++) {
	// il[i] = ll.get(i);
	// }
	// return il;
	// }
	
} // end class RubberBandingListener

```

## 8/23/2019

https://github.com/aterai/java-swing-tips/tree/master/DragSelectDropReordering/src/java/example

http://coderazzi.net/tablefilter/download.html

http://repo2.maven.org/maven2/net/coderazzi/tablefilter-swing/5.5.1/tablefilter-swing-5.5.1.pom
```
    <groupId>net.coderazzi</groupId>
    <artifactId>tablefilter-swing</artifactId>
    <packaging>jar</packaging>
    <version>5.5.1</version>
```

https://tips4java.wordpress.com/2008/11/18/row-number-table/

https://www.codejava.net/java-se/swing/6-techniques-for-sorting-jtable-you-should-know

https://www.aspose.com/   
^^^ File Format APIs ; this looks very good; not sure about price yet

### DataBrowser.java
```
    private void updateRowHeights_fast() {
    	int rowcount = 1; // tblCsvData.getRowCount()
        for (int row = 0; row < rowcount; row++)
        {
            int rowHeight = tblCsvData.getRowHeight(); 
            if (!printedCellSize)
                System.out.print("row height before: " + rowHeight);

            //assume first column is sufficient to adjust the entire row
            for (int column = 0; column < 1; column++) {
                Component comp = tblCsvData.prepareRenderer(tblCsvData.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            if (!printedCellSize)
                System.out.println(", after: " + rowHeight);
            
            tblCsvData.setRowHeight(rowHeight); tblCsvData.setRowHeight(row, rowHeight);

            printedCellSize = true;
        }
    }
```

## 8/22/2019

https://www.javaworld.com/article/2073002/jtable-filtering-with-glazed-lists.html
### EventSelectionModel
Java's DefaultListSelectionModel has a flaw that prevents it from cooperating with filtering. When a row is inserted into a JTable, it becomes selected if immediately inserted before a selected row. This proves problematic if the rows of a JTable are selected when its filter changes. When the filter is removed, some restored rows may incorrectly become selected.

Glazed Lists includes EventSelectionModel, which has more predictable behavior. This class also has a method, getEventList(), which contains the JTable's current selection. As rows are selected, the corresponding elements are automatically added to that list.

### DataBrowser
```
        case 4: // case 3 but refactored to use glazed lists column sorting
        {
        	GlazedListTableModel tm = new GlazedListTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = tm;

        	final SortedList sorted = new SortedList(tm.getEventList(), new DataComparator());
        	
        	final FilterList filtered = new FilterList(sorted, new TextComponentMatcherEditor(txtFilter, new DataTextFilterator(dataHintAware)));
        	
        	AdvancedTableModel etm = GlazedListsSwing.eventTableModelWithThreadProxyList(filtered, tm);
        	csvTableModel = etm;
            tblCsvData.setModel(csvTableModel); // for the next line to work, you must set the model first

        	TableComparatorChooser<Object> tcc = TableComparatorChooser.install(tblCsvData, sorted, TableComparatorChooser.SINGLE_COLUMN);

        }
        	break;
        default:
        	;
        }
        
        switch(modeldesign) {
        case 4:
        	// already setModel() in case 4 above
        	break;
        default:
        	tblCsvData.setModel(csvTableModel);
        }
```

### org.jwellman.csvviewer.glazed.DataComparator
```
package org.jwellman.csvviewer.glazed;

import java.util.Comparator;

/**
 * 
 * @author rwellman
 *
 */
public class DataComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		String[] objectA = (String[]) o1;
		String[] objectB = (String[]) o2;
		
		int comparison = 0;
		try {
			// index 0 is the line number (once I implement line numbers in the underlying data)
			Integer intA = Integer.parseInt( objectA[0] );
			Integer intB = Integer.parseInt( objectB[0] );
			comparison = intA - intB;
		} catch (Throwable t) {
			// If it is not a number, it's a string
			comparison = objectA[0].compareTo(objectB[0]);
		}
		
		return comparison;
	}

}
```

## 8/21/2019
Filtered JTable
* new GlazedListTableModel (org.jwellman.csvviewer)
* new DataTextFilterator (org.jwellman.csvviewer.glazed)
* mod DataBrowser

### DataBrowser
```
	private TableModel csvTableModel;
	
	// This was not originally present; however, with the addition of glazed lists
	// it became necessary to differentiate between DelimitedFileTableModel and GlazedListTableModel
	private DataHintAware dataHintAware;
...
    private JTextField txtFilter;
...
        int modeldesign = 3;
        switch(modeldesign) {
        case 1:
        	csvTableModel = new DelimitedFileTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = (DataHintAware) csvTableModel;

        	break;
        case 2:
        {
        	GlazedListTableModel tm = new GlazedListTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = tm;
        	DefaultEventTableModel etm = new DefaultEventTableModel(tm.getEventList(), tm);
        	csvTableModel = etm;
        }
        	break;
        case 3: // case 2 + filter list
        {
        	GlazedListTableModel tm = new GlazedListTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = tm;
        	FilterList filteredData = new FilterList(tm.getEventList(), new TextComponentMatcherEditor(txtFilter, new DataTextFilterator(dataHintAware)));
        	DefaultEventTableModel etm = new DefaultEventTableModel(filteredData, tm);
        	csvTableModel = etm;
        }
        	break;
        default:
        	;
        }
        tblCsvData.setModel(csvTableModel);
		    tblCsvData.setShowVerticalLines(false);
...    
    	    north.add(txtFilter = new JTextField("enter-filter-here"));

```

### org.jwellman.csvviewer.GlazedListTableModel
```
package org.jwellman.csvviewer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;

/**
 * 
 * @author rwellman
 *
 */
public class GlazedListTableModel implements DataHintAware, TableFormat<Object> {
	
	private static final long serialVersionUID = 1L;

	private List<String> columns = new ArrayList<>();

	private List<DataHint> dataHints = new ArrayList<>();

	private List<String[]> records;
	
	private EventList<Object> glazedrecords = new BasicEventList<>();
	
	public GlazedListTableModel(File file, String delimiter) {

		Reader reader = null;
		CSVReader csvReader = null;

		try {

			reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset());

			final CSVParser parser = new CSVParserBuilder()
			.withSeparator((delimiter.equals("\\t")) ? '\t' : delimiter.charAt(0))
			.withIgnoreQuotations(false)
			.build();

			csvReader = new CSVReaderBuilder(reader)
			.withSkipLines(0)
			.withCSVParser(parser)
			.build();

			records = csvReader.readAll();

			// TODO curious... how does this affect performance for large datasets?
			boolean fix = true;
			if (fix) {
				for (String[] record : records) {
					for (int i=0; i < record.length; i++) {
						String field = record[i];
						record[i] = field.trim();
					}
				}
			}
			
			// Assume first record is column headings; get them, remove them from dataset, specify NUMERIC data hint
			List<String> columnHeadings = new ArrayList<>(records.get(0).length);
			columnHeadings.add("<#>"); // TODO make this an application property
			columnHeadings.addAll(Arrays.asList(records.get(0)));			
			columns.addAll(columnHeadings);
			records.remove(0); // remove the column headings record
			dataHints.add(DataHint.NUMERIC); // though numeric, treat line numbers as strings

			// Determine data hint for remaining columns;
			final String[] record = records.get(0);
			for (String field : record) {
				if (NumberUtils.isCreatable(field)) {
					dataHints.add(DataHint.NUMERIC);
				} else {
					dataHints.add(DataHint.STRING);
				}
			}

			// GlazedLists
			glazedrecords.addAll(records);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (csvReader != null)
					csvReader.close();

				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public List<DataHint> getDataHints() {
		return dataHints;
	}

	@Override
	public int getColumnCount() {
		return records.get(0).length + 1;
	}

	@Override
	public String getColumnName(int col) {
		return (columns != null) ? columns.get(col) : "TBD";
	}

	@Override
	public Object getColumnValue(Object baseObject, int column) {
		if (column == 0)
			return 1; // TODO how to map to row index now that I'm using glazed lists?
			// idea; probably have to modify each underlying String[] object to have the record number
			// not really what I *want* to do, but it might now be my only option
		else {
			String[] asarray = (String[])baseObject;
			return asarray[column - 1]; 			
		}
	}

	public EventList<Object> getEventList() {
		return glazedrecords;
	}
	
}
```

### org.jwellman.csvviewer.glazed.DataTextFilterator
```
package org.jwellman.csvviewer.glazed;

import java.util.List;
import java.util.ArrayList;

import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;

import ca.odell.glazedlists.TextFilterator;

/**
 * An implementation of TextFilterator to support our generic data viewer.
 * 
 * Note: (From the glazed lists website...)
 * The getFilterStrings() method is awkward because the List of Strings is a parameter rather than the return type. This approach allows Glazed Lists to skip creating an ArrayList each time the method is invoked.
 * We're generally averse to this kind of micro-optimization. In this case this performance improvement is worthwhile because the method is used heavily while filtering.
 * 
 * 
 * @author rwellman
 *
 */
public class DataTextFilterator implements TextFilterator {

	private Integer[] textidx;
	
	public DataTextFilterator(DataHintAware aware) {
		final List<Integer> indices = new ArrayList<>();		
		final List<DataHint> hints = aware.getDataHints();
		
		int column = 0;
		for (DataHint hint : hints) {
			if (hint == DataHint.STRING) {
				indices.add(column);
			}
			column++;
		}
		
		textidx = indices.stream().toArray(Integer[] :: new);
	}
	
	@Override
	public void getFilterStrings(List baseList, Object element) {
		final String[] asarray = (String[]) element;
		for (int ptr : textidx) {
			baseList.add(asarray[ptr-1]); // the hints are 1 greater than the data array because of the "line number"
		}
	}

}

```
