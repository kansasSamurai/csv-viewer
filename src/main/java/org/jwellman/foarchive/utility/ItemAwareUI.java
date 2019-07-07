package org.jwellman.foarchive.utility;

import java.util.List;

/**
 *
 * @author Rick
 */
public interface ItemAwareUI {

    /**
     * Allows the DnD Listener to trigger a repaint.
     */
    public void repaint();

    /**
     * Allows the DnD Listener to specify which item is being manipulated.
     *
     * @param m
     */
    public void setDragItem(Moveable m);

    /**
     * Allows the DnD Listener to query which item is being manipulated;
     * i.e. after being previously set
     *
     * @return
     */
    public Moveable getDragItem();

    /**
     * Makes the list of Moveable(s) available to the DnD Listener.
     *
     * @return List of Moveable(s)
     */
    public List<Moveable> getItems();

    /**
     * Allows to explicitly set the location of a Moveable.
     * This is most commonly used when the mouse is released after dragging
     * and the new location is logically bound to a specific region.
     * i.e. the player released the item somewhere over the region but the
     * item needs to be placed exactly within the region
     *
     * @param m a Moveable item
     * @param x
     * @param y
     */
    public void setItemLocation(Moveable m, int x, int y);

    /**
     * Allows the UI to determine if the item is moveable based on state.
     * i.e. In a game scenario, does the state of the game allow the current
     * item to be moved.
     * 
     * @param item
     * @return
     */
    public boolean isItemMoveable(Moveable item);

}
