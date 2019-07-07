package org.jwellman.farchive.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class ItemsDragAndDropListener implements MouseListener, MouseMotionListener {

    // The panel/ui to which we are listening.
    final private ItemAwareUI gui;

    // The List of Moveable(s) managed by the panel/ui.
    final private List<Moveable> pieces;

    // The x and y offsets for dragging
    private int dragOffsetX, dragOffsetY;

    final private static boolean logenabled = false; // simple logging gate

    public ItemsDragAndDropListener(ItemAwareUI ui) {
        this.gui = ui;
        this.pieces = ui.getItems();
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        final int x = evt.getPoint().x; p(x);
        final int y = evt.getPoint().y; p(y);

        // find out which piece to move.
        // we check the list from top to buttom
        // (therefore we itereate in reverse order)
        for (int i = this.pieces.size() - 1; i >= 0; i--) {
            final Moveable piece = this.pieces.get(i);
            if (!piece.isMovable()) continue;

            if (mouseOverPiece(piece, x, y) && gui.isItemMoveable(piece)) {
		// calculate offset, because we do not want the drag piece
                // to jump with it's upper left corner to the current mouse
                // position
                this.dragOffsetX = x - piece.getX();
                this.dragOffsetY = y - piece.getY();

                gui.setDragItem(piece);
                this.pieces.remove(piece); // [A]
                this.pieces.add(piece); // [A]

                gui.repaint();

                break;
            }
        }

        // move drag piece to the top of the list
        // this was original location of [A]
        //        if (this.dragPiece != null) {
        //            this.pieces.remove(this.dragPiece);
        //            this.pieces.add(this.dragPiece);
        //        }

    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        if (gui.getDragItem() != null) {
            int x = evt.getPoint().x - this.dragOffsetX;
            int y = evt.getPoint().y - this.dragOffsetY;

            gui.setItemLocation(gui.getDragItem(), x, y);
            gui.repaint();
            gui.setDragItem(null);
        }
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        final Moveable m = gui.getDragItem();
        if (m != null) {
            m.setX(evt.getPoint().x - this.dragOffsetX);
            m.setY(evt.getPoint().y - this.dragOffsetY);
            gui.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        // We do not (yet?) handle clicks but this
        // is some sample code if we ever do:
//        if (evt.getClickCount() == 2) {
//            System.out.println("double clicked");
//        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    private void p(Object s) {
        if (logenabled) System.out.println(s);
    }

    /**
     * Determine if the mouse is currently over this piece
     *
     * @param piece the playing piece
     * @param x x coordinate of mouse
     * @param y y coordinate of mouse
     * @return true if mouse is over the piece
     */
    private boolean mouseOverPiece(Moveable piece, int x, int y) {
        final boolean inX = piece.getX() <= x && piece.getX() + piece.getWidth() >= x ;
        final boolean inY = piece.getY() <= y && piece.getY() + piece.getHeight() >= y;
        final boolean isOver = inX && inY;
        if (isOver) { p(true); p(x); p(piece.getWidth()); p(y); p(piece.getHeight()); }
        return isOver;
    }

}
