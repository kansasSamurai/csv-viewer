package org.jwellman.swing.mouse;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.stream.IntStream;

import javax.swing.JList;
import javax.swing.event.MouseInputAdapter;

/**
 * This is a generic mouse listener that can be used used on any JList.
 * 
 * Note that this class is not directly responsible for drawing the rubberband;
 * rather, it just defines the dimensions accessible via the getRubberBand() accessor.
 * This is a good design as it allows your JList paintComponent() method
 * to completely control the visual design of the rubberband -- color, outline, etc.
 * As a convenience, this class does provide an example working paint algorithm via
 * the paint() method -- see javadocs for details.
 * 
 * Even though this class does not DRAW the rubberband, 
 * this class *does* actually select the list items that are
 * within the bounds of the rubber band.
 * 
 * @author rwellman
 *
 */
public class RubberBandingListener extends MouseInputAdapter {
	
	private final Point srcPoint = new Point();

	private final Path2D rubberBand = new Path2D.Double();

	private static final AlphaComposite ALPHA = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f);

	/**
	 * This class does not draw the rubberband, it just defines its dimensions.
	 * For the ateria demo, see ReorderableList::paintComponent() for the actual draw.
	 * 
	 * @return
	 */
	public Path2D getRubberBand() {
		return rubberBand;
	}
	
	/**
	 * An example paint algorithm; this can be used if sufficient for your application.
	 * If you decide to use a different algorithm, you can either put graphics natives
	 * in your JList paintComponent() [suggested] or override this method in a subclass
	 * if desired.
	 * 
	 * @param g2
	 * @param border
	 * @param fill
	 */
	public void paint(Graphics2D g2, Color border, Color fill) {
		
		// draw the border
		g2.setPaint(border);
		g2.draw(this.getRubberBand());
		
		// fill the border
		g2.setPaint(fill);
		g2.setComposite(ALPHA);
		g2.fill(this.getRubberBand());
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		final Component jc = e.getComponent();
		
		final JList<?> jlist;
	    if (jc instanceof JList) {
			jlist = (JList<?>) jc;
			if (jlist.getDragEnabled()) {
				return;
			}			
			
			this.updatePath(e);
			
			// JDK 1.7.0: l.setSelectedIndices(getIntersectsIcons(l, rubberBand));
			int[] indices = IntStream.range( 0, jlist.getModel().getSize() )
					.filter(i -> this.getRubberBand().intersects(jlist.getCellBounds(i, i))).toArray();
			// System.out.println("idc: " + indices);
			jlist.setSelectedIndices(indices); // << this may prove problematic if you have a list selection listener as I think it will fire with every mousedrag event
//			jlist.repaint();			
		} else {
			this.updatePath(e);
		}		

	    jc.repaint();
	}

	private void updatePath(MouseEvent e) {
		// Update the path based on "source point" vs. "event point"
		final Point destPoint = e.getPoint();		

		final Path2D rb = this.getRubberBand();
		rb.reset();
		rb.moveTo(srcPoint.x, srcPoint.y);
		rb.lineTo(destPoint.x, srcPoint.y);
		rb.lineTo(destPoint.x, destPoint.y);
		rb.lineTo(srcPoint.x, destPoint.y);
		rb.closePath();		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// if (Objects.isNull(srcPoint) || !getDragEnabled()) {
		// Component glassPane = l.getRootPane().getGlassPane();
		// glassPane.setVisible(false);
		// }
		this.getRubberBand().reset();

		final JList<?> jlist;
	    if (e.getSource() instanceof JList) {
			jlist = (JList<?>) e.getSource();
			jlist.setFocusable(true);
			jlist.setDragEnabled(jlist.getSelectedIndices().length > 0);
			jlist.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
//		final JList<?> jlist;
//	    if (e.getSource() instanceof JList) {
//			jlist = (JList<?>) e.getComponent();
//			int index = jlist.locationToIndex(e.getPoint());
//			Rectangle rect = jlist.getCellBounds(index, index);
//			if (rect.contains(e.getPoint())) {
//				jlist.setFocusable(true);
//				if (jlist.getDragEnabled()) {
//					return;
//				}
//				// System.out.println("ccc:" + startSelectedIndex);
//				jlist.setSelectedIndex(index);
//			} else {
//				jlist.clearSelection();
//				jlist.getSelectionModel().setAnchorSelectionIndex(-1);
//				jlist.getSelectionModel().setLeadSelectionIndex(-1);
//				jlist.setFocusable(false);
//				jlist.setDragEnabled(false);
//			}
//			jlist.repaint();
//	    }
//		
		// By design, this is the only spot that srcPoint is initialized
		srcPoint.setLocation(e.getPoint());
		
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
